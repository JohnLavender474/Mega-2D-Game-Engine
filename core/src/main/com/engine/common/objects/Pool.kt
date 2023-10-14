package com.engine.common.objects

import com.badlogic.gdx.utils.Array

/**
 * A pool of objects. This pool is not thread-safe.
 *
 * @param T the type of object to pool
 * @param startAmount the initial amount of objects in the pool
 * @param supplier the supplier of the objects
 * @param onSupplyNew a runnable that will be called when a new object is instantiated from the
 *   supplier
 * @param onFetch a runnable that will be called when an object is fetched from the pool
 * @param onPool a runnable that will be called when an object is pooled
 */
class Pool<T>(
    startAmount: Int = 0,
    var supplier: () -> T,
    var onSupplyNew: ((T) -> Unit)? = null,
    var onFetch: ((T) -> Unit)? = null,
    var onPool: ((T) -> Unit)? = null
) {

  private val queue = Array<T>()

  init {
    for (i in 0 until startAmount) pool(supplyNew())
  }

  /**
   * Fetch an object from the pool. If the pool is empty, a new object will be supplied.
   *
   * @return an object from the pool
   */
  fun fetch(): T {
    val element = if (queue.isEmpty) supplyNew() else queue.pop()
    onFetch?.invoke(element)
    return element
  }

  /**
   * Pool an object. This will call the [onPool] runnable if it is not null.
   *
   * @param element the object to pool
   */
  fun pool(element: T) {
    queue.add(element)
    onPool?.invoke(element)
  }

  private fun supplyNew(): T {
    val element = supplier()
    onSupplyNew?.invoke(element)
    return element
  }
}
