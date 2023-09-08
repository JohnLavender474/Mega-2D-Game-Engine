package com.engine.common.interfaces

/**
 * A containable is an object that can be contained in another object.
 *
 * @param T The type of the container
 */
fun interface Containable<T> {

  /**
   * Checks if this object is contained in the given container.
   *
   * @param container The container
   * @return True if this object is contained in the given container, false otherwise
   */
  fun isContainedIn(container: T): Boolean
}
