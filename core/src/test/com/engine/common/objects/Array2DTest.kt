package com.engine.common.objects

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.spyk

class Array2DTest :
    DescribeSpec({
      describe("Array2DIterator") {
        lateinit var iterator: Array2DIterator<Int?>

        beforeEach {
          val array2D = Array2D<Int?>(3, 3)
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          iterator = Array2DIterator(array2D)
        }

        it("should iterate through elements correctly") {
          val elements = mutableListOf<Int?>()
          while (iterator.hasNext()) {
            elements.add(iterator.next())
          }
          elements shouldContainAll listOf(null, 1, 2, 3)
        }

        it("should throw NoSuchElementException when there are no more elements") {
          for (i in 0..8) {
            println(i)
            iterator.next()
          }
          shouldThrow<NoSuchElementException> { iterator.next() }
        }
      }

      describe("Array2D") {
        lateinit var array2D: Array2D<Int?>

        beforeEach { array2D = spyk(Array2D(3, 4)) }

        it("should have the correct initial properties") {
          array2D.rows shouldBe 3
          array2D.columns shouldBe 4
          array2D.size shouldBe 0
          array2D.isEmpty() shouldBe true
        }

        describe("add") {
          it("should add elements correctly") {
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            array2D.size shouldBe 3
            array2D.isEmpty() shouldBe false
          }

          it("should add all elements correctly") {
            array2D.addAll(listOf(1, 2, 3))

            array2D.size shouldBe 3
            array2D.isEmpty() shouldBe false
          }
        }

        it("should get elements correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          array2D[0, 0] shouldBe 1
          array2D[1, 1] shouldBe 2
          array2D[2, 2] shouldBe 3
        }

        describe("remove") {
          it("should remove elements correctly") {
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            array2D.remove(2)

            array2D.contains(2) shouldBe false
            array2D.size shouldBe 2
            array2D.isEmpty() shouldBe false
          }

          it("should remove all elements correctly") {
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            array2D.removeAll(listOf(1, 2, 3))

            array2D.contains(1) shouldBe false
            array2D.contains(2) shouldBe false
            array2D.contains(3) shouldBe false
            array2D.size shouldBe 0
            array2D.isEmpty() shouldBe true
          }
        }

        it("should retain all correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          array2D.retainAll(listOf(2, 3))

          array2D.contains(1) shouldBe false
          array2D.contains(2) shouldBe true
          array2D.contains(3) shouldBe true
          array2D.size shouldBe 2
          array2D.isEmpty() shouldBe false
        }

        it("should clear correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          array2D.clear()

          array2D.contains(1) shouldBe false
          array2D.contains(2) shouldBe false
          array2D.contains(3) shouldBe false
          array2D.size shouldBe 0
          array2D.isEmpty() shouldBe true
        }

        it("should get indexes correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 1

          array2D.getIndexes(1) shouldContainAll listOf(Pair(0, 0), Pair(2, 2))
          array2D.getIndexes(2) shouldContainAll listOf(Pair(1, 1))
        }

        describe("contains") {
          it("should check if contains correctly") {
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 1

            array2D.contains(1) shouldBe true
            array2D.contains(2) shouldBe true
            array2D.contains(3) shouldBe false
          }

          it("should check if contains all correctly") {
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 1

            array2D.containsAll(listOf(1, 2)) shouldBe true
            array2D.containsAll(listOf(1, 2, 3)) shouldBe false
          }
        }

        it("should map correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          val mappedArray2D = array2D.map { it?.times(2) }
          mappedArray2D[0, 0] shouldBe 2
          mappedArray2D[1, 1] shouldBe 4
          mappedArray2D[2, 2] shouldBe 6

          array2D[0, 0] shouldBe 1
          array2D[1, 1] shouldBe 2
          array2D[2, 2] shouldBe 3
        }

        it("should apply for each correctly") {
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          val set = HashSet<Int?>()
          array2D.forEach { _, element -> set.add(element?.times(2)) }

          set shouldContainAll setOf(2, 4, 6)
        }
      }
    })
