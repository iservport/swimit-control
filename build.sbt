name := "swimit-control"

scalaVersion := "2.11.4"

version := "1.0.1"

mainClass in Compile := Some("com.iservport.swimit.akka.Bootstrap")

// add dependencies

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-xml" % "2.11.0-M4",
  "com.typesafe.akka" %% "akka-kernel" % "2.3.8",
  "com.typesafe.akka" %% "akka-actor" % "2.3.8",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.8",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-M2",
  "org.ccil.cowan.tagsoup" % "tagsoup"               % "1.2",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.6",
  "joda-time" % "joda-time" % "2.7",
  "org.slf4j" % "slf4j-simple" % "1.7.12",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)

// add resolvers

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

// append several options to the list of options passed to the Java compiler

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-encoding", "UTF8")

