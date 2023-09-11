package com.engine.common.interfaces

/** A function that returns true if the test is passed. */
fun interface UpdatePredicate {

  /**
   * Runs the test and returns true if the test is passed.
   *
   * @param delta the time in seconds since the last update
   */
  fun test(delta: Float): Boolean
}
