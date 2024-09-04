package com.mega.game.engine.common.objects

/**
 * Represents a pair of integers.
 *
 * @property first The first integer in the pair.
 * @property second The second integer in the pair.
 */
data class IntPair(val first: Int, val second: Int) {

    /**
     * Adds an integer value to both components of this pair.
     *
     * @param value The integer value to add.
     * @return A new IntPair with the addition operation applied.
     */
    operator fun plus(value: Int) = IntPair(first + value, second + value)

    /**
     * Subtracts an integer value from both components of this pair.
     *
     * @param value The integer value to subtract.
     * @return A new IntPair with the subtraction operation applied.
     */
    operator fun minus(value: Int) = IntPair(first - value, second - value)

    /**
     * Multiplies both components of this pair by an integer value.
     *
     * @param value The integer value to multiply by.
     * @return A new IntPair with the multiplication operation applied.
     */
    operator fun times(value: Int) = IntPair(first * value, second * value)

    /**
     * Divides both components of this pair by an integer value.
     *
     * @param value The integer value to divide by.
     * @return A new IntPair with the division operation applied.
     */
    operator fun div(value: Int) = IntPair(first / value, second / value)

    /**
     * Adds the components of another IntPair to this pair.
     *
     * @param other The IntPair to add.
     * @return A new IntPair with the addition operation applied.
     */
    operator fun plus(other: IntPair): IntPair = IntPair(first + other.first, second + other.second)

    /**
     * Subtracts the components of another IntPair from this pair.
     *
     * @param other The IntPair to subtract.
     * @return A new IntPair with the subtraction operation applied.
     */
    operator fun minus(other: IntPair): IntPair = IntPair(first - other.first, second - other.second)

    /**
     * Multiplies the components of another IntPair with this pair.
     *
     * @param other The IntPair to multiply with.
     * @return A new IntPair with the multiplication operation applied.
     */
    operator fun times(other: IntPair): IntPair = IntPair(first * other.first, second * other.second)

    /**
     * Divides the components of another IntPair with this pair.
     *
     * @param other The IntPair to divide by.
     * @return A new IntPair with the division operation applied.
     */
    operator fun div(other: IntPair): IntPair = IntPair(first / other.first, second / other.second)
}

/**
 * Creates an IntPair from two integer values.
 *
 * @param that The second integer to create the IntPair.
 * @return An IntPair containing the provided integer values.
 */
infix fun Int.pairTo(that: Int): IntPair = IntPair(this, that)
