enablePlugins(ScalaJSPlugin)

name := "Keter"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "utest" % "0.3.1",
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "org.webjars" % "rot.js" % "0.5.0"
)

jsDependencies += "org.webjars" % "rot.js" % "0.5.0" / "rot.min.js"

// We don't want to test JS dependencies:
jsDependencyFilter in Test := { case _ => List() }

testFrameworks += new TestFramework("utest.runner.Framework")

skip in packageJSDependencies := false

lazy val sitePath = settingKey[String]("Directory for the site")

lazy val site = taskKey[Unit]("Publish site task")

sitePath := "site"

site := {
  import java.nio.file.{Files, StandardCopyOption}
  (fastOptJS in Compile).value
  val targetDirectory = target.value / sitePath.value
  val sourceJS = target.value / "scala-2.11" / "keter-fastopt.js"
  val sourceMap = target.value / "scala-2.11" / "keter-fastopt.js.map"
  val sourceJSDeps = target.value / "scala-2.11" / "keter-jsdeps.js"
  val targetJS = targetDirectory / "keter.js"
  val targetMap = targetDirectory / sourceMap.name
  val targetJSDeps = targetDirectory / sourceJSDeps.name
  targetJS.mkdirs()
  Files.copy(sourceJS.toPath, targetJS.toPath, StandardCopyOption.REPLACE_EXISTING)
  Files.copy(sourceMap.toPath, targetMap.toPath, StandardCopyOption.REPLACE_EXISTING)
  Files.copy(sourceJSDeps.toPath, targetJSDeps.toPath, StandardCopyOption.REPLACE_EXISTING)
  (resourceDirectory in Compile).value.listFiles() foreach { file =>
    val targetFile = targetDirectory / file.name
    Files.copy(file.toPath, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
  }
}
