name := """mtls-example"""
organization := "uk.gov.hmrc"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += "com.github.tomakehurst" % "wiremock"      % "3.0.0-beta-7" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "uk.gov.hmrc.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "uk.gov.hmrc.binders._"
