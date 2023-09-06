package com.engine.common.objects

data class IntPair(val x: Int, val y: Int)

infix fun Int.to(that: Int): IntPair = IntPair(this, that)
