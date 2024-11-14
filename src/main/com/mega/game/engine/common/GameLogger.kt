package com.mega.game.engine.common

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.utils.ObjectSet

/**
 * The log level. The higher the ordinal, the more important the log level is.
 *
 * @see [GameLogger]
 */
enum class GameLogLevel {
    OFF,
    LOG,
    DEBUG,
    ERROR
}

/**
 * Definition class assigned to a [GameLogLevel] value.
 *
 * @property filterByTag whether the logs of the log level should be filtered by tags
 * @property tagsToLog the logs to print (with all others ignored) if and only if [filterByTag] is true
 */
data class GameLogDef(
    var filterByTag: Boolean = false,
    val tagsToLog: ObjectSet<String> = ObjectSet()
)

/**
 * A simple logger that can be used to log messages. The log level can be set to determine which
 * messages should be logged. If the log level is set to [GameLogLevel.LOG], only info messages
 * will be logged. If the log level is set to [GameLogLevel.DEBUG], info and debug messages will be
 * logged. If the log level is set to [GameLogLevel.ERROR], info, debug, and error messages will be
 * logged. If the logger is turned off, no messages will be logged.
 *
 * @see [GameLogLevel]
 */
object GameLogger : ApplicationLogger {

    val DEFAULT_LOG_FORMATTER: (level: GameLogLevel, tag: String, message: String, throwable: Throwable?) -> String =
        { level, tag, message, throwable ->
            var message = "$level | $tag | $message"
            if (throwable != null) {
                message += " | ${throwable.message}"
                throwable.stackTrace.forEach { line -> message += "\n\t$line" }
            }
            message
        }

    val tagsToLog = ObjectSet<String>()
    var filterByTag = true

    internal var formatter: (level: GameLogLevel, tag: String, message: String, throwable: Throwable?) -> String =
        DEFAULT_LOG_FORMATTER
    internal var level = GameLogLevel.OFF

    /**
     * Sets the log level. The log level determines which messages should be logged. If the log level
     * is set to [GameLogLevel.LOG], only info messages will be logged. If the log level is set to
     * [GameLogLevel.DEBUG], info and debug messages will be logged. If the log level is set to
     * [GameLogLevel.ERROR], info, debug, and error messages will be logged.
     *
     * @param level the log level
     */
    fun setLogLevel(level: GameLogLevel) {
        this.level = level
    }

    /**
     * Gets the current log level.
     *
     * @return the current log level
     */
    fun getLogLevel() = level

    /**
     * Sets the formatter for formatting logs.
     *
     * @param formatter the formatter to set for formatting logs
     */
    fun setLogFormatter(
        formatter: (level: GameLogLevel, tag: String, message: String, throwable: Throwable?) -> String
    ) {
        this.formatter = formatter
    }

    override fun log(tag: String, message: String) = print(GameLogLevel.LOG, tag, message)

    override fun log(tag: String, message: String, exception: Throwable) =
        print(GameLogLevel.LOG, tag, message, exception)

    override fun debug(tag: String, message: String) = print(GameLogLevel.DEBUG, tag, message)

    override fun debug(tag: String, message: String, exception: Throwable?) =
        print(GameLogLevel.DEBUG, tag, message, exception)

    override fun error(tag: String, message: String) = print(GameLogLevel.ERROR, tag, message)

    override fun error(tag: String, message: String, exception: Throwable?) =
        print(GameLogLevel.ERROR, tag, message, exception)

    private fun print(level: GameLogLevel, tag: String, message: String, throwable: Throwable? = null) {
        if (level == GameLogLevel.OFF ||
            this.level.ordinal > level.ordinal ||
            (filterByTag && !tagsToLog.contains(tag))
        ) return

        val string = formatter.invoke(level, tag, message, throwable)
        println(string)
    }
}
