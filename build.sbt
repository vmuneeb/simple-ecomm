name := "template-api-rest-java-playframework"

version := "1.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,PlayEnhancer)

autoScalaLibrary := false

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.18",
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.8.5"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
