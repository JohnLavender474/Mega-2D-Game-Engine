package com.engine.common

import com.badlogic.gdx.utils.ObjectSet

/**
 * The log level. The higher the ordinal, the more important the log level is.
 *
 * @see [GameLogger]
 */
enum class GameLogLevel {
  INFO,
  DEBUG,
  ERROR
}

/**
 * A simple logger that can be used to log messages. The log level can be set to determine which
 * messages should be logged. If the log level is set to [GameLogLevel.INFO], only info messages
 * will be logged. If the log level is set to [GameLogLevel.DEBUG], info and debug messages will be
 * logged. If the log level is set to [GameLogLevel.ERROR], info, debug, and error messages will be
 * logged. If the logger is turned off, no messages will be logged.
 *
 * @see [GameLogLevel]
 */
object GameLogger {

  /**
   * Whether the logger should filter messages by tag. If this is set to true, only messages with
   * tags that are in [tagsToLog] will be logged. Default value is false.
   */
  var filterByTag = false

  /**
   * The set of tags that should be logged if [filterByTag] is set to true. If [filterByTag] is set
   * to true, only messages with tags that are in this set will be logged. If [filterByTag] is set
   * to false, this set will be ignored.
   */
  val tagsToLog = ObjectSet<String>()

  internal var logLevel: GameLogLevel? = null

  /** Turns off the logger. No messages will be logged. */
  fun turnOff() {
    logLevel = null
  }

  /**
   * Sets the log level. The log level determines which messages should be logged. If the log level
   * is set to [GameLogLevel.INFO], only info messages will be logged. If the log level is set to
   * [GameLogLevel.DEBUG], info and debug messages will be logged. If the log level is set to
   * [GameLogLevel.ERROR], info, debug, and error messages will be logged.
   *
   * @param level the log level
   * @see [GameLogLevel]
   */
  fun set(level: GameLogLevel) {
    logLevel = level
  }

  /**
   * Logs the given message if the log level is set to [GameLogLevel.INFO] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun info(tag: String, message: String) = log(GameLogLevel.INFO, tag, message)

  /**
   * Logs the given message if the log level is set to [GameLogLevel.DEBUG] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun debug(tag: String, message: String) = log(GameLogLevel.DEBUG, tag, message)

  /**
   * Logs the given message if the log level is set to [GameLogLevel.ERROR] or higher.
   *
   * @param message the message to log
   * @return true if the message was logged, false otherwise
   */
  fun error(tag: String, message: String) = log(GameLogLevel.ERROR, tag, message)

  private fun log(level: GameLogLevel, tag: String, message: String): Boolean {
    if (filterByTag && !tagsToLog.contains(tag)) return false

    logLevel?.let {
      if (level.ordinal <= it.ordinal) {
        println("[$level] --- [$tag] --- $message")
        return true
      }
    }

    return false
  }
}
