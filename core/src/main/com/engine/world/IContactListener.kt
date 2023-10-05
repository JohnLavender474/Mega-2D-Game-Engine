package com.engine.world

/**
 * An interface for contact listeners. Contact listeners are called when a [Fixture] begins,
 * continues, or ends contact with another [Fixture].
 */
interface IContactListener {

  /**
   * Called when a [Fixture] begins contact with another [Fixture].
   *
   * @param f1 the first [Fixture]
   * @param f2 the second [Fixture]
   * @param delta the delta time
   */
  fun beginContact(f1: Fixture, f2: Fixture, delta: Float)

  /**
   * Called when a [Fixture] continues contact with another [Fixture].
   *
   * @param f1 the first [Fixture]
   * @param f2 the second [Fixture]
   * @param delta the delta time
   */
  fun continueContact(f1: Fixture, f2: Fixture, delta: Float)

  /**
   * Called when a [Fixture] ends contact with another [Fixture].
   *
   * @param f1 the first [Fixture]
   * @param f2 the second [Fixture]
   * @param delta the delta time
   */
  fun endContact(f1: Fixture, f2: Fixture, delta: Float)
}
