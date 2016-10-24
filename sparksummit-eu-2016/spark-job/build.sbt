name := "spark-job"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.1" % "provided",
  "org.apache.spark" %% "spark-streaming" % "2.0.1" % "provided",
  "com.github.scopt" %% "scopt" % "3.5.0"
)


