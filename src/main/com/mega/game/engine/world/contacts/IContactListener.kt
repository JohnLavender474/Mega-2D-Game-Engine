package com.mega.game.engine.world.contacts

/**
 * An interface for contact listeners. Contact listeners are called when a [Fixture] begins,
 * continues, or ends contact with another [Fixture].
 */
interface IContactListener {

    /**
     * Called when a [Fixture] begins contact with another [Fixture].
     *
     * @param contact the [Contact]
     * @param delta the delta time
     */
    fun beginContact(contact: Contact, delta: Float)

    /**
     * Called when a [Fixture] continues contact with another [Fixture].
     *
     * @param contact the [Contact]
     * @param delta the delta time
     */
    fun continueContact(contact: Contact, delta: Float)

    /**
     * Called when a [Fixture] ends contact with another [Fixture].
     *
     * @param contact the [Contact]
     * @param delta the delta time
     */
    fun endContact(contact: Contact, delta: Float)
}
