package com.mega.game.engine.common.extensions

import com.badlogic.gdx.math.MathUtils
import kotlin.math.pow

/**
 * Computes the exponential of a given value using the base of the natural logarithm (Euler's number, e).
 *
 * This function raises the mathematical constant e (approximately 2.71828) to the power of the given [value].
 *
 * @param value The exponent to which e should be raised. A positive value results in e raised to a positive power,
 *              while a negative value results in e raised to a negative power (which produces a fractional result).
 * @return The result of e raised to the power of [value].
 */
fun exp(value: Float) = MathUtils.E.pow(value)
