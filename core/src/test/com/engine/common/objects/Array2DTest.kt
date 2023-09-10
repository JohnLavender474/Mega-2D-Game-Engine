package com.engine.common.objects

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
          array2D.size shouldBe 12
        }

        describe("out of bounds") {
          it("should return true for row out of bounds") {
            array2D.isOutOfBounds(3, 0) shouldBe true
          }

          it("should return true for column out of bounds") {
            array2D.isOutOfBounds(0, 4) shouldBe true
          }

          it("should return false for row out of bounds") {
            array2D.isOutOfBounds(2, 0) shouldBe false
          }

          it("should return false for column out of bounds") {
            array2D.isOutOfBounds(0, 3) shouldBe false
          }

          it("should throw IndexOutOfBoundsException when getting an element") {
            shouldThrow<IndexOutOfBoundsException> { array2D[3, 4] }
          }

          it("should throw IndexOutOfBoundsException when setting an element") {
            shouldThrow<IndexOutOfBoundsException> { array2D.set(3, 4, 1) }
          }
        }

        describe("set") {
          it("should set elements correctly - test 1") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            // when
            for (i in 0 until 3) {
              for (j in 0 until 3) {
                // then
                when (i pairTo j) {
                  0 pairTo 0 -> {
                    array2D[i, j] shouldBe 1
                  }
                  1 pairTo 1 -> {
                    array2D[i, j] shouldBe 2
                  }
                  2 pairTo 2 -> {
                    array2D[i, j] shouldBe 3
                  }
                  else -> {
                    array2D[i, j] shouldBe null
                  }
                }
              }
            }
          }

          it("should set elements correctly - test 2") {
            // if
            array2D[0, 0] = 1
            array2D[0, 0] = 2

            // when
            val element = array2D[0, 0]
            val array2DMap = array2D.array2DMap
            val elementToIndexMap = array2D.elementToIndexMap
            val set1 = elementToIndexMap[1]
            val set2 = elementToIndexMap[2]

            // then
            element shouldBe 2

            array2DMap.size shouldBe 1
            array2DMap[0 pairTo 0] shouldBe 2

            elementToIndexMap.size shouldBe 1

            set1 shouldBe null
            set2 shouldNotBe null
            set2?.shouldContain((0 pairTo 0))
          }

          it("should set elements correctly - test 3") {
            // if
            array2D[0, 0] = 1
            array2D[0, 0] = null

            // when
            val element = array2D[0, 0]
            val array2DMap = array2D.array2DMap
            val elementToIndexMap = array2D.elementToIndexMap
            val set1 = elementToIndexMap[1]

            // then
            element shouldBe null
            array2DMap.size shouldBe 0
            array2DMap[0 pairTo 0] shouldBe null
            elementToIndexMap.size shouldBe 0
            set1 shouldBe null
          }

          it("should set elements correctly - test 4") {
            // if
            val oldVal1 = (array2D.set(0, 0, 1))
            val oldVal2 = (array2D.set(0, 0, null))
            val oldVal3 = (array2D.set(0, 0, 1))

            // when
            val element = array2D[0, 0]
            val array2DMap = array2D.array2DMap
            val elementToIndexMap = array2D.elementToIndexMap
            val set1 = elementToIndexMap[1]

            // then
            oldVal1 shouldBe null
            oldVal2 shouldBe 1
            oldVal3 shouldBe null

            element shouldBe 1

            array2DMap.size shouldBe 1
            array2DMap[0 pairTo 0] shouldBe 1

            elementToIndexMap.size shouldBe 1

            set1 shouldNotBe null
            set1?.shouldContain(0 pairTo 0)
          }
        }

        describe("add") {
          it("should add all elements correctly") {
            // if
            array2D.addAll(listOf(1, 2, 3))

            // then
            for (i in 1..3) {
              array2D.contains(i) shouldBe true
              array2D[0, i - 1] shouldBe i
            }
          }
        }

        it("should get elements correctly") {
          // if
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          // then
          array2D[0, 0] shouldBe 1
          array2D[1, 1] shouldBe 2
          array2D[2, 2] shouldBe 3
        }

        describe("remove") {
          it("should remove elements correctly") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            // when
            array2D.remove(2)

            // then
            for (i in 0 until 3) {
              array2D.contains(i + 1) shouldBe (i + 1 != 2)
              array2D[i, i] shouldBe if (i != 1) i + 1 else null

              val elementSet = array2D.elementToIndexMap[i + 1]
              elementSet shouldBe if (i != 1) setOf(i pairTo i) else null
              elementSet?.shouldContain(if (i != 1) i pairTo i else null)

              array2D.array2DMap[i pairTo i] shouldBe if (i != 1) i + 1 else null
            }
          }

          it("should remove all elements correctly") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 3

            // when
            array2D.removeAll(listOf(1, 2, 3))

            // then
            array2D.contains(1) shouldBe false
            array2D.contains(2) shouldBe false
            array2D.contains(3) shouldBe false
            for (i in 0 until 3) {
              array2D[i, i] shouldBe null
              array2D.elementToIndexMap[i] shouldBe null
              array2D.array2DMap[i pairTo i] shouldBe null
            }
          }
        }

        it("should retain all correctly") {
          // if
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          // when
          array2D.retainAll(listOf(2, 3))

          // then
          array2D.contains(1) shouldBe false
          array2D.contains(2) shouldBe true
          array2D.contains(3) shouldBe true
          array2D[0, 0] shouldBe null
          array2D[1, 1] shouldBe 2
          array2D[2, 2] shouldBe 3
        }

        it("should clear correctly") {
          // if
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          // when
          array2D.clear()

          // then
          array2D.contains(1) shouldBe false
          array2D.contains(2) shouldBe false
          array2D.contains(3) shouldBe false
          for (i in 0 until 3) {
            array2D[i, i] shouldBe null
            array2D.elementToIndexMap[i] shouldBe null
            array2D.array2DMap[i pairTo i] shouldBe null
          }
        }

        describe("get indexes") {
          it("should get indexes correctly - test 1") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 1

            // when
            val set1 = array2D.getIndexes(1)
            val set2 = array2D.getIndexes(2)

            // then
            set1 shouldContainAll listOf(0 pairTo 0, 2 pairTo 2)
            set2 shouldContainAll listOf(1 pairTo 1)
          }

          it("should get indexes correctly - test 2") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[0, 0] = 2

            // when
            val set1 = array2D.getIndexes(1)
            val set2 = array2D.getIndexes(2)

            // then
            set1.isEmpty() shouldBe true
            set2.size shouldBe 2
            set2 shouldContainAll listOf(0 pairTo 0, 1 pairTo 1)
          }

          it("should get indexes correctly - test 3") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2

            // when
            val set = array2D.getIndexes(null)

            // then
            set.isEmpty() shouldBe false
            set.size shouldBe (3 * 4) - 2
            set.all { it != 0 pairTo 0 && it != 1 pairTo 1 } shouldBe true

            for (i in 0 until 3) {
              for (j in 0 until 4) {
                val pair = i pairTo j

                set.contains(pair) shouldBe (pair != 0 pairTo 0 && pair != 1 pairTo 1)
              }
            }
          }
        }

        describe("contains") {
          it("should check if contains correctly") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 1

            // then
            array2D.contains(1) shouldBe true
            array2D.contains(2) shouldBe true
            array2D.contains(3) shouldBe false
          }

          it("should check if contains all correctly") {
            // if
            array2D[0, 0] = 1
            array2D[1, 1] = 2
            array2D[2, 2] = 1

            // then
            array2D.containsAll(listOf(1, 2)) shouldBe true
            array2D.containsAll(listOf(1, 2, 3)) shouldBe false
          }
        }

        it("should map correctly") {
          // if
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          // when
          val mappedArray2D = array2D.map { it?.times(2) }
          mappedArray2D[0, 0] shouldBe 2
          mappedArray2D[1, 1] shouldBe 4
          mappedArray2D[2, 2] shouldBe 6

          // then
          array2D[0, 0] shouldBe 1
          array2D[1, 1] shouldBe 2
          array2D[2, 2] shouldBe 3
        }

        it("should apply for each correctly") {
          // if
          array2D[0, 0] = 1
          array2D[1, 1] = 2
          array2D[2, 2] = 3

          // when
          val set = HashSet<Int?>()
          array2D.forEach { _, element -> set.add(element?.times(2)) }

          // then
          set shouldContainAll setOf(2, 4, 6)
        }

        it("should construct correctly with matrix") {
          // if
          val matrix = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6))

          // when
          val _array2D = Array2D(matrix)
          println(_array2D)

          // then
          _array2D.rows shouldBe 2
          _array2D.columns shouldBe 3

          for (x in 0 until 2) {
            for (y in 0 until 3) {
              _array2D[x, y] shouldBe matrix[x][y]
            }
          }
        }

        it("should construct correctly with horizontally flipped matrix") {
          // if
          /*
          1 2 3
          4 5 6
          7 8 9
          */
          val matrix = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6), arrayOf(7, 8, 9))

          // when
          val _array2D = Array2D(matrix, flipVertically = false, flipHorizontally = true)
          println(_array2D)

          // then
          /*
          3 2 1
          6 5 4
          9 8 7
          */
          _array2D[0, 0] shouldBe 3
          _array2D[0, 1] shouldBe 2
          _array2D[0, 2] shouldBe 1

          _array2D[1, 0] shouldBe 6
          _array2D[1, 1] shouldBe 5
          _array2D[1, 2] shouldBe 4

          _array2D[2, 0] shouldBe 9
          _array2D[2, 1] shouldBe 8
          _array2D[2, 2] shouldBe 7
        }

        it("should construct correctly with vertically flipped matrix") {
          // if
          /*
          1 2 3
          4 5 6
          7 8 9
          */
          val matrix = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6), arrayOf(7, 8, 9))

          // when
          val _array2D = Array2D(matrix, flipVertically = true, flipHorizontally = false)
          println(_array2D)

          // then
          /*
           7 8 9
           4 5 6
           1 2 3
          */
          _array2D[0, 0] shouldBe 7
          _array2D[0, 1] shouldBe 8
          _array2D[0, 2] shouldBe 9

          _array2D[1, 0] shouldBe 4
          _array2D[1, 1] shouldBe 5
          _array2D[1, 2] shouldBe 6

          _array2D[2, 0] shouldBe 1
          _array2D[2, 1] shouldBe 2
          _array2D[2, 2] shouldBe 3
        }

        it("should construct correctly with flipped matrix") {
          // if
          /*
          1 2 3
          4 5 6
          7 8 9
          */
          val matrix = arrayOf(arrayOf(1, 2, 3), arrayOf(4, 5, 6), arrayOf(7, 8, 9))

          // if
          val _array2D = Array2D(matrix, flipVertically = true, flipHorizontally = true)
          println(_array2D)

          // then
          /*
          9 8 7
          6 5 4
          3 2 1
          */
          _array2D[0, 0] shouldBe 9
          _array2D[0, 1] shouldBe 8
          _array2D[0, 2] shouldBe 7

          _array2D[1, 0] shouldBe 6
          _array2D[1, 1] shouldBe 5
          _array2D[1, 2] shouldBe 4

          _array2D[2, 0] shouldBe 3
          _array2D[2, 1] shouldBe 2
          _array2D[2, 2] shouldBe 1
        }
      }
    })
