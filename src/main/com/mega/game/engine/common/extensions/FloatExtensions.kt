package com.mega.game.engine.common.extensions

/**
 * Rounds this [Float] to the given number of [decimals].
 *
 * @param decimals the number of decimals to round to
 * @return this [Float] rounded to the given number of [decimals]
 */
fun Float.round(decimals: Int = 2) = "%.${decimals}f".format(this).toFloat()
