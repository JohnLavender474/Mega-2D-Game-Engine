package com.engine

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

      describe("Logger") {
        context("Default log level") { it("should be null") { Logger.logLevel shouldBe null } }

        context("Setting log level to INFO") {
          beforeTest { Logger.set(LogLevel.INFO) }

          it("should set log level to INFO") { Logger.logLevel shouldBe LogLevel.INFO }

          it("should log INFO messages") {
            Logger.info("Info message")
            outputStreamCaptor.toString() shouldContain "Info message"
          }

          it("should not log DEBUG messages") {
            Logger.debug("Debug message")
            outputStreamCaptor.toString() shouldNotBe "Debug message"
          }
        }

        context("Setting log level to DEBUG") {
          beforeTest { Logger.set(LogLevel.DEBUG) }

          it("should set log level to DEBUG") { Logger.logLevel shouldBe LogLevel.DEBUG }

          it("should log INFO messages") {
            Logger.info("Info message")
            outputStreamCaptor.toString() shouldContain "Info message"
          }

          it("should log DEBUG messages") {
            Logger.debug("Debug message")
            outputStreamCaptor.toString() shouldContain "Debug message"
          }

          it("should not log ERROR messages") {
            Logger.error("Error message")
            outputStreamCaptor.toString() shouldNotBe "Error message"
          }
        }

        context("Turning off the logger") {
          beforeTest { Logger.turnOff() }

          it("should set log level to null") { Logger.logLevel shouldBe null }

          it("should not log any messages") {
            Logger.info("Info message")
            Logger.debug("Debug message")
            Logger.error("Error message")
            outputStreamCaptor.toString() shouldBe ""
          }
        }
      }
    })
