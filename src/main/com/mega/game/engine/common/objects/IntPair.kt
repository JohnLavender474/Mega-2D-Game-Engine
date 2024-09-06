package com.mega.game.engine.common.objects

import com.badlogic.gdx.math.Vector2

/**
 * Represents a pair of integers.
 *
 * @property x The first integer in the pair.
 * @property y The second integer in the pair.
 */
data class IntPair(val x: Int, val y: Int) {

    /**
     * Adds an integer value to both components of this pair.
     *
     * @param value The integer value to add.
     * @return A new IntPair with the addition operation applied.
     */
    operator fun plus(value: Int) = IntPair(x + value, y + value)

    /**
     * Subtracts an integer value from both components of this pair.
     *
     * @param value The integer value to subtract.
     * @return A new IntPair with the subtraction operation applied.
     */
    operator fun minus(value: Int) = IntPair(x - value, y - value)

    /**
     * Multiplies both components of this pair by an integer value.
     *
     * @param value The integer value to multiply by.
     * @return A new IntPair with the multiplication operation applied.
     */
    operator fun times(value: Int) = IntPair(x * value, y * value)

    /**
     * Divides both components of this pair by an integer value.
     *
     * @param value The integer value to divide by.
     * @return A new IntPair with the division operation applied.
     */
    operator fun div(value: Int) = IntPair(x / value, y / value)

    /**
     * Adds the components of another IntPair to this pair.
     *
     * @param other The IntPair to add.
     * @return A new IntPair with the addition operation applied.
     */
    operator fun plus(other: IntPair): IntPair = IntPair(x + other.x, y + other.y)

    /**
     * Subtracts the components of another IntPair from this pair.
     *
     * @param other The IntPair to subtract.
     * @return A new IntPair with the subtraction operation applied.
     */
    operator fun minus(other: IntPair): IntPair = IntPair(x - other.x, y - other.y)

    /**
     * Multiplies the components of another IntPair with this pair.
     *
     * @param other The IntPair to multiply with.
     * @return A new IntPair with the multiplication operation applied.
     */
    operator fun times(other: IntPair): IntPair = IntPair(x * other.x, y * other.y)

    /**
     * Divides the components of another IntPair with this pair.
     *
     * @param other The IntPair to divide by.
     * @return A new IntPair with the division operation applied.
     */
    operator fun div(other: IntPair): IntPair = IntPair(x / other.x, y / other.y)

    /**
     * Converts this [IntPair] into a [Vector2] by converting the [x] and [y] values to [Float].
     *
     * @return a [Vector2] representation of this [IntPair]
     */
    fun toVector2() = Vector2(x.toFloat(), y.toFloat())
}

/**
 * Creates an IntPair from two integer values.
 *
 * @param that The second integer to create the IntPair.
 * @return An IntPair containing the provided integer values.
 */
infix fun Int.pairTo(that: Int): IntPair = IntPair(this, that)
