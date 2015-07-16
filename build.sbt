name := "postgres"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.scalatest" %% "scalatest" % "2.2.4"
)