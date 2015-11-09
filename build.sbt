name := """Pawnshop"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  ws,
  specs2 % Test
)

libraryDependencies += "org.mongodb" % "casbah_2.11" % "3.0.0"
libraryDependencies += "jp.t2v" %% "play2-auth" % "0.14.1"
libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.1"
libraryDependencies += "com.typesafe.play" % "play-mailer_2.11" % "4.0.0-M1"
libraryDependencies += "com.hazelcast" % "hazelcast" % "3.5.3"

resolvers += DefaultMavenRepository
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

//javaOptions ++= Seq(
  //"-Dhttps.port=443",
  //"-Dhttps.keyStore=conf/cloud-lombard.jks",
  //"-Dhttps.keyStorePassword=Kestroday1987",)