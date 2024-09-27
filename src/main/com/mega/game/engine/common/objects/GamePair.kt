package com.mega.game.engine.common.objects

/**
 * Data class for mutable pairs (to replace Kotlin's GamePair class).
 */
data class GamePair<K, V>(var first: K, var second: V) {

    /**
     * Sets the pair values.
     *
     * @param first the first value
     * @param second the second value
     * @return this pair for chaining
     */
    fun set(first: K, second: V): GamePair<K, V> {
        this.first = first
        this.second = second
        return this
    }
}

infix fun <A, B> A.pairTo(that: B): GamePair<A, B> = GamePair(this, that)