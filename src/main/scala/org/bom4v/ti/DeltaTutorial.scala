package org.bom4v.ti

//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs._

/**
  * Spark job aimed at testing basic features of Delta Lake.
  */
object DeltaLakeTutorial extends App {
  //
  val spark = org.apache.spark.sql.SparkSession
    .builder()
    .appName("DeltaLakeTutorial")
    .master("local[*]")
    .enableHiveSupport()
    .getOrCreate()

  // Display versions
  val versionScala:String = util.Properties.versionString
  val versionSpark:String = spark.version
  println ("Spark: " + versionSpark + "  -  Scala: " + versionScala)

  // Create a table
  val dataV0 = spark.range (0, 5)

  // Store the data into Delta Lake
  dataV0.write
    .format ("delta")
    .save ("/tmp/delta-lake/table-5-int.dlk")

  // Update the table data
  val dataV1 = spark.range (5, 10)

  // Store the updated version of the data into Delta Lake
  dataV1.write
    .format ("delta")
    .mode ("overwrite")
    .save ("/tmp/delta-lake/table-5-int.dlk")

  // Read (latest version of the) data from Delta Lake
  val dfLatest = spark.read
    .format ("delta")
    .load ("/tmp/delta-lake/table-5-int.dlk")

  println ("Latest version of the data:")
  dfLatest.show()

  // Read older versions of data from Delta Lake using time travel
  val dfOlder = spark.read
    .format ("delta")
    .option ("versionAsOf", 0)
    .load ("/tmp/delta-lake/table-5-int.dlk")

  println ("Older version of the data:")
  dfOlder.show()

  // Merge tests
  val deltaTable = io.delta.tables
    .DeltaTable
    .forPath ("/tmp/delta-lake/table-5-int.dlk")

  // Update every even value by adding 100 to it
  deltaTable.update(
    condition = org.apache.spark.sql.functions.expr ("id % 2 == 0"),
    set = Map ("id" -> org.apache.spark.sql.functions.expr ("id + 100")))

  // Delete every even value
  deltaTable.delete(condition = org.apache.spark.sql.functions.expr ("id % 2 == 0"))

  // Upsert (merge) new data
  val newData = spark.range(0, 20).as("newData").toDF

  deltaTable.as("oldData")
    .merge(
      newData,
      "oldData.id = newData.id")
    .whenMatched
    .update(Map("id" -> org.apache.spark.sql.functions.col("newData.id")))
    .whenNotMatched
    .insert(Map("id" -> org.apache.spark.sql.functions.col("newData.id")))
    .execute()

  println ("After conditional merge:")
  deltaTable.toDF.show()

  // History
  val fullHistoryDF = deltaTable.history()
  println ("History of table updates:")
  fullHistoryDF.show()

  // End of the Spark session
  spark.stop()
}

