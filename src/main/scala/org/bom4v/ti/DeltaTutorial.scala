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
  dataV0.write.format("delta").save("/tmp/delta-table")

  // Update the table data
  val dataV1 = spark.range (5, 10)
  dataV1.write.format("delta").mode("overwrite").save("/tmp/delta-table")

  // Read data
  val dfLatest = spark.read.format("delta").load("/tmp/delta-table")
  println ("Latest version of the data:")
  dfLatest.show()

  // Read older versions of data using time travel
  val dfOlder = spark.read.format("delta").option("versionAsOf", 0).load("/tmp/delta-table")
  println ("Older version of the data:")
  dfOlder.show()

  // End of the Spark session
  spark.stop()
}


