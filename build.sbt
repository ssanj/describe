name := "describe"

organization := "net.ssanj"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scala-lang" %  "scala-reflect"  % "2.11.8",
  "org.scala-lang" %  "scala-compiler" % "2.11.8",
  "org.scalatest"  %% "scalatest"      % "3.0.0"  % "test",
  "org.scalacheck" %% "scalacheck"     % "1.13.4" % "test"
)

scalacOptions ++= Seq(
                      "-unchecked",
                      "-deprecation",
                      "-feature",
                      "-Xfatal-warnings",
                      "-Xlint:_",
                      "-Ywarn-dead-code",
                      "-Ywarn-inaccessible",
                      "-Ywarn-unused-import",
                      "-Ywarn-infer-any",
                      "-Ywarn-nullary-override",
                      "-Ywarn-nullary-unit",
                      "-language:implicitConversions",
                      "-language:higherKinds"
                     )

scalacOptions in (Compile, console) ~= (_ filterNot (Seq("-Ywarn-unused-import", "-Xfatal-warnings").contains(_)))

scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

scalacOptions in (Compile,doc) ++= Seq("-no-link-warnings")

initialCommands in (Compile, console) := "import net.ssanj.describe._"

