name := "future-shop-backend"

version := "1.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,PlayEnhancer)

autoScalaLibrary := false

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  cache,
  javaWs,
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.amazonaws" % "aws-java-sdk" % "1.11.109",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.8.5",
  "org.apache.pdfbox" % "pdfbox" % "2.0.4",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "net.coobird" % "thumbnailator" % "0.4.8"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
