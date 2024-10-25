package com.mega.game.engine.common.extensions

import kotlin.math.abs

/**
 * Rounds this [Float] to the given number of [decimals]. The default value of [decimals] is 2.
 *
 * @param decimals the number of decimals to round to
 * @return this [Float] rounded to the given number of [decimals]
 */
fun Float.round(decimals: Int = 2) = "%.${decimals}f".format(this).toFloat()

/**
 * Returns true if the [other] value is within the [epsilon] range of this value.
 *
 * @param other the other float value
 * @param epsilon the epsilon range
 * @return true if the other value is within the epsilon range of this value
 */
fun Float.epsilonEquals(other: Float, epsilon: Float) = abs(other - this) <= epsilon
