scalaJSSettings

name := "Keter"

scalaVersion := "2.11.2"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"

lazy val sitePath = settingKey[String]("Directory for the site")

lazy val site = taskKey[Unit]("Publish site task")

sitePath := "site"

site := {
  import java.nio.file.{Files, StandardCopyOption}
  (ScalaJSKeys.fastOptJS in Compile).value
  val targetDirectory = target.value / sitePath.value
  val sourceJS = target.value / "scala-2.11" / "keter-fastopt.js"
  val sourceMap = target.value / "scala-2.11" / "keter-fastopt.js.map"
  val targetJS = targetDirectory / "keter.js"
  val targetMap = targetDirectory / sourceMap.name
  targetJS.mkdirs()
  Files.copy(sourceJS.toPath, targetJS.toPath, StandardCopyOption.REPLACE_EXISTING)
  Files.copy(sourceMap.toPath, targetMap.toPath, StandardCopyOption.REPLACE_EXISTING)
  (resourceDirectory in Compile).value.listFiles() foreach { file =>
    val targetFile = targetDirectory / file.name
    Files.copy(file.toPath, targetFile.toPath, StandardCopyOption.REPLACE_EXISTING)
  }
}