package com.mega.game.engine.damage

/**
 * A [IDamageable] is an object that can take damage from a [IDamager]. The [IDamageable] is
 * responsible for determining whether it can take damage from a [IDamager] and how much damage it
 * takes.
 */
interface IDamageable {

    /** Return true if this [IDamageable] is invincible, otherwise false. */
    val invincible: Boolean

    /**
     * Return true if this [IDamageable] can take damage from the [IDamager], otherwise false. Default return
     * return value is true.
     *
     * @param damager the [IDamager] that is dealing damage to this [IDamageable]
     * @return true if this [IDamageable] can take damage from the [IDamager], otherwise false
     */
    fun canBeDamagedBy(damager: IDamager): Boolean = true

    /**
     * Take damage from the [IDamager]. Return true if damage was taken, otherwise false. Default return
     * value is false.
     *
     * @param damager the [IDamager] that is dealing damage to this [IDamageable]
     * @return true if damage was taken, otherwise false
     */
    fun takeDamageFrom(damager: IDamager): Boolean = false
}
