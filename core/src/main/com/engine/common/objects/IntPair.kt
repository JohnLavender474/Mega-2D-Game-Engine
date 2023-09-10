package com.engine.common.objects

data class IntPair(val first: Int, val second: Int) {

  operator fun plus(value: Int) = IntPair(first + value, second + value)

  operator fun minus(value: Int) = IntPair(first - value, second - value)

  operator fun times(value: Int) = IntPair(first * value, second * value)

  operator fun div(value: Int) = IntPair(first / value, second / value)

  operator fun plus(other: IntPair): IntPair = IntPair(first + other.first, second + other.second)

  operator fun minus(other: IntPair): IntPair = IntPair(first - other.first, second - other.second)

  operator fun times(other: IntPair): IntPair = IntPair(first * other.first, second * other.second)

  operator fun div(other: IntPair): IntPair = IntPair(first / other.first, second / other.second)
}

infix fun Int.pairTo(that: Int): IntPair = IntPair(this, that)
