package com.engine.common

/**
 * The log level. The higher the ordinal, the more important the log level is.
 *
 * @see [Logger]
 */
enum class LogLevel {
  INFO,
  DEBUG,
  ERROR,
}

/**
 * A simple logger that can be used to log messages. The log level can be set to determine which
 * messages should be logged. If the log level is set to [LogLevel.INFO], only info messages will be
 * logged. If the log level is set to [LogLevel.DEBUG], info and debug messages will be logged. If
 * the log level is set to [LogLevel.ERROR], info, debug, and error messages will be logged. If the
 * logger is turned off, no messages will be logged.
 *
 * @see [LogLevel]
 */
object Logger {

  internal var logLevel: LogLevel? = null

  /** Turns off the logger. No messages will be logged. */
  fun turnOff() {
    logLevel = null
  }

  /**
   * Sets the log level. The log level determines which messages should be logged. If the log level
   * is set to [LogLevel.INFO], only info messages will be logged. If the log level is set to
   * [LogLevel.DEBUG], info and debug messages will be logged. If the log level is set to
   * [LogLevel.ERROR], info, debug, and error messages will be logged.
   *
   * @param level the log level
   * @see [LogLevel]
   */
  fun set(level: LogLevel) {
    logLevel = level
  }

  /**
   * Logs the given message if the log level is set to [LogLevel.INFO] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun info(message: String) = log(LogLevel.INFO, message)

  /**
   * Logs the given message if the log level is set to [LogLevel.DEBUG] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun debug(message: String) = log(LogLevel.DEBUG, message)

  /**
   * Logs the given message if the log level is set to [LogLevel.ERROR] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun error(message: String) = log(LogLevel.ERROR, message)

  private fun log(level: LogLevel, message: String): Boolean {
    logLevel?.let {
      if (level.ordinal <= it.ordinal) {
        println(message)
        return true
      }
    }

    return false
  }
}
