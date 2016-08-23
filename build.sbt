lazy val artifactSettings = Seq(
  organization := "uk.gov.ons.business-register",
  version := "1.0.0-SNAPSHOT",
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.10.6",
  // Scala / Java options
  scalacOptions ++= Seq("-deprecation", "-unchecked"),
  javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
  // Repositories
  resolvers ++= Seq(
    Resolver.defaultLocal,
    Resolver.mavenLocal
  )
)

// Modules

val sparkVersion = "1.6.2"
val sparkDependencyScope = "compile"

val root = Project("business-core", file("."))
  .settings(artifactSettings: _*)
  .settings(buildSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % sparkDependencyScope,
      "org.apache.spark" %% "spark-sql" % sparkVersion % sparkDependencyScope,
      // test dependencies
      "uk.gov.ons.business-register" %% "test-utils" % "1.0.0-SNAPSHOT" % "test"
    )
  )