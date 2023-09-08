package com.engine.common.objects

data class IntPair(val x: Int, val y: Int) {

  operator fun plus(value: Int) = IntPair(x + value, y + value)

  operator fun minus(value: Int) = IntPair(x - value, y - value)

  operator fun times(value: Int) = IntPair(x * value, y * value)

  operator fun div(value: Int) = IntPair(x / value, y / value)

  operator fun plus(other: IntPair): IntPair = IntPair(x + other.x, y + other.y)

  operator fun minus(other: IntPair): IntPair = IntPair(x - other.x, y - other.y)

  operator fun times(other: IntPair): IntPair = IntPair(x * other.x, y * other.y)

  operator fun div(other: IntPair): IntPair = IntPair(x / other.x, y / other.y)
}

infix fun Int.pairTo(that: Int): IntPair = IntPair(this, that)
