package com.engine

import com.engine.common.GameLogLevel
import com.engine.common.GameLogger
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LoggerTest :
    DescribeSpec({
        val standardOut = System.out
        val outputStreamCaptor = ByteArrayOutputStream()

        beforeTest { System.setOut(PrintStream(outputStreamCaptor)) }

        afterTest {
            System.setOut(standardOut)
            outputStreamCaptor.reset()
        }

        describe("GameLogger") {
            context("Default log level") { it("should be null") { GameLogger.logLevel shouldBe null } }

            context("Setting log level to INFO") {
                beforeTest { GameLogger.set(GameLogLevel.INFO) }

                it("should set log level to INFO") { GameLogger.logLevel shouldBe GameLogLevel.INFO }

                it("should log INFO messages") {
                    GameLogger.info("TEST", "Info message")
                    outputStreamCaptor.toString() shouldContain "Info message"
                }

                it("should not log DEBUG messages") {
                    GameLogger.debug("TEST", "Debug message")
                    outputStreamCaptor.toString() shouldNotBe "Debug message"
                }
            }

            context("Setting log level to DEBUG") {
                beforeTest { GameLogger.set(GameLogLevel.DEBUG) }

                it("should set log level to DEBUG") { GameLogger.logLevel shouldBe GameLogLevel.DEBUG }

                it("should log INFO messages") {
                    GameLogger.info("TEST", "Info message")
                    outputStreamCaptor.toString() shouldContain "Info message"
                }

                it("should log DEBUG messages") {
                    GameLogger.debug("TEST", "Debug message")
                    outputStreamCaptor.toString() shouldContain "Debug message"
                }

                it("should not log ERROR messages") {
                    GameLogger.error("TEST", "Error message")
                    outputStreamCaptor.toString() shouldNotBe "Error message"
                }
            }

            context("Turning off the logger") {
                beforeTest { GameLogger.turnOff() }

                it("should set log level to null") { GameLogger.logLevel shouldBe null }

                it("should not log any messages") {
                    GameLogger.info("TEST", "Info message")
                    GameLogger.debug("TEST", "Debug message")
                    GameLogger.error("TEST", "Error message")
                    outputStreamCaptor.toString() shouldBe ""
                }
            }
        }
    })
