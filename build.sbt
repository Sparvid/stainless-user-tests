val scala3Version = "3.6.4"
val scala2Version = "2.13.12"

lazy val root = project
  .in(file("."))
  .settings(
    name := "fib",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    Compile / unmanagedJars += file("lib/stainless-library.jar")
  )