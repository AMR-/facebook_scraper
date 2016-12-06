name := "scraper"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.3",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.8",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "net.liftweb" % "lift-json_2.11" % "3.0-M8"
)

lazy val scrape = TaskKey[Unit]("scrape", "Runs the scraper to scrape facebook pages as specified in the config json and properties file.")
lazy val parse = InputKey[Unit]("parse", "Run the parser on the data files in the specified directory / from the specified day.")
lazy val merge = TaskKey[Unit]("merge", "Merge all the csvs in the parser directory into a single csv in the merged directory, removing duplicates.")

fullRunTask(scrape, Compile, "ScrapeFacebook")
fullRunInputTask(parse, Compile, "ParseData")
fullRunTask(merge, Compile, "MergeCsvs")
