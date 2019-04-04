
name := "gatling-dubbo"

version := "2.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % "2.3.1" % "provided",
  "com.alibaba" % "dubbo" % "2.6.5",
//  "com.youzan.xxx" % "xxx-api" % "1.xxx-RELEASE" exclude("ch.qos.logback", "logback-classic") //应用 API 包
)

// Gatling contains scala-library
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
