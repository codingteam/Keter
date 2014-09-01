package ru.org.codingteam.keter.util

/**
 * Trait which initializes instance of logger with object's classname.
 */
trait Logging {
  lazy val log = new Logging.Logger(getClass.getName)

}

object Logging {

  /**
   * Simple logging implementation.
   * It should be replaced by (or should delegate to) logger from some framework like scala-logging.
   */
  class Logger(name: String) {

    object Level extends Enumeration {
      val Debug, Info, Warning, Error = Value
    }

    // TODO: load log level and additional configuration from config.
    val level = Level.Debug

    val isDebugEnabled = level <= Level.Debug
    val isInfoEnabled = level <= Level.Info
    val isWarningEnabled = level <= Level.Warning
    val isErrorEnabled = level <= Level.Error

    val out = System.out

    /** Name in converted form (e.g. java.util.List => j.u.List).  */
    lazy val convertedName = {
      val components = name split "\\."
      components.take(components.size - 1).map(_ take 1) :+ components.last mkString "."
    }

    // TODO: the above methods should be a macros (like in scala-logging).

    def debug(msg: => String): Unit = {
      if (isDebugEnabled) {
        out.println(s"DEBUG $convertedName: $msg")
      }
    }

    def debug(msg: => String, cause: Throwable): Unit = {
      if (isDebugEnabled) {
        out.println(s"DEBUG $convertedName: $msg")
        cause.printStackTrace(out)
      }
    }

    def info(msg: => String): Unit = {
      if (isInfoEnabled) {
        out.println(s"INFO $convertedName: $msg")
      }
    }

    def info(msg: => String, cause: Throwable): Unit = {
      if (isInfoEnabled) {
        out.println(s"INFO $convertedName: $msg")
        cause.printStackTrace(out)
      }
    }

    def warn(msg: => String): Unit = {
      if (isWarningEnabled) {
        out.println(s"WARN $convertedName: $msg")
      }
    }

    def warn(msg: => String, cause: Throwable): Unit = {
      if (isWarningEnabled) {
        out.println(s"WARN $convertedName: $msg")
        cause.printStackTrace(out)
      }
    }

    def error(msg: => String): Unit = {
      if (isErrorEnabled) {
        out.println(s"ERROR $convertedName: $msg")
      }
    }

    def error(msg: => String, cause: Throwable): Unit = {
      if (isErrorEnabled) {
        out.println(s"ERROR $convertedName: $msg")
        cause.printStackTrace(out)
      }
    }

  }

}
