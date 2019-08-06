name := "delta-tutorial"

organization := "org.bom4v.ti"

organizationName := "Business Object Models for Verticals (BOM4V)"

organizationHomepage := Some(url("http://github.com/bom4v"))

version := "0.0.2-spark2.4"

homepage := Some(url("https://github.com/bom4v/induction-data-lake"))

startYear := Some(2019)

description := "Delta Lake tutorial (in Scala and Python)"

licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")

scmInfo := Some(
  ScmInfo(
    url("https://github.com/bom4v/induction-data-lake"),
    "https://github.com/bom4v/induction-data-lake.git"
  )
)

developers := List(
  Developer(
    id    = "denis.arnaud",
    name  = "Denis Arnaud",
    email = "denis.arnaud_ossrh@m4x.org",
    url   = url("https://github.com/denisarnaud")
  )
)

//useGpg := true

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12", "2.12.8")

checksums in update := Nil

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.22.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.7.0" % "test"

libraryDependencies += "io.delta" %% "delta-core" % "0.3.0"

// Spark
libraryDependencies ++= (version.value match {
  case v if v.contains("spark2.3") => Seq(
    "org.apache.spark" %% "spark-core" % "2.3.2",
    "org.apache.spark" %% "spark-sql" % "2.3.2",
    "org.apache.spark" %% "spark-mllib" % "2.3.2",
    "org.apache.spark" %% "spark-hive" % "2.3.2"
  )
  case v if v.contains("spark2.4") => Seq(
    "org.apache.spark" %% "spark-core" % "2.4.3",
    "org.apache.spark" %% "spark-sql" % "2.4.3",
    "org.apache.spark" %% "spark-mllib" % "2.4.3",
    "org.apache.spark" %% "spark-hive" % "2.4.3"
  )
  case _ => Seq()
})

javacOptions in Compile ++= Seq("-source", "1.8",  "-target", "1.8")

scalacOptions ++= Seq("-deprecation", "-feature")

// Hortonworks HDP Hive 3 (having a static method in an interface)
scalacOptions ++= (version.value match {
  case v if v.contains("hive3hdp") => Seq("-target:jvm-1.8")
  case _ => Seq()
})

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)

publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

cleanKeepFiles += target.value / "test-reports"


