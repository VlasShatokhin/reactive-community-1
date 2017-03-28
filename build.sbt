name := "reactive-streams"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "io.reactivex.rxjava2" % "rxjava" % "2.0.7",
  "com.typesafe.akka" % "akka-stream_2.12" % "2.4.17",
  "com.typesafe.akka" % "akka-http_2.12" % "10.0.4",
  "io.projectreactor" % "reactor-core" % "3.0.3.RELEASE")
