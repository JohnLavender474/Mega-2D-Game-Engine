package com.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.engine.common.GameLogger
import com.engine.common.interfaces.Initializable

/**
 * A pool of objects. This pool is not thread-safe.
 *
 * @param T the type of object to pool
 * @param startAmount the initial amount of objects in the pooll; default is 10
 * @param supplier the supplier of the objects
 * @param onSupplyNew a runnable that will be called when a new object is instantiated from the
 *   supplier
 * @param onFetch a runnable that will be called when an object is fetched from the pool
 * @param onPool a runnable that will be called when an object is pooled
 */
class Pool<T>(
    private var startAmount: Int = 10,
    var supplier: () -> T,
    var onSupplyNew: ((T) -> Unit)? = null,
    var onFetch: ((T) -> Unit)? = null,
    var onPool: ((T) -> Unit)? = null
) : Initializable {

    companion object {
        const val TAG = "Pool"
    }

    private var initialized = false
    private val queue = Array<T>()

    /** Initialize the pool by supplying the initial amount of objects. */
    override fun init() {
        GameLogger.debug(TAG, "Initializing pool")
        for (i in 0 until startAmount) pool(supplyNew())
        initialized = true
    }

    /**
     * Fetch an object from the pool. If the pool is empty, a new object will be supplied.
     *
     * @return an object from the pool
     */
    fun fetch(): T {
        if (!initialized) init()
        val element = if (queue.isEmpty) supplyNew() else queue.pop()
        GameLogger.debug(
            TAG, "Fetched object from pool. Updated queue size: ${queue.size}. Element: $element"
        )
        onFetch?.invoke(element)
        return element
    }

    /**
     * Pool an object. This will call the [onPool] runnable if it is not null.
     *
     * @param element the object to pool
     */
    fun pool(element: T) {
        GameLogger.debug(TAG, "Pooling object: $element")
        queue.add(element)
        onPool?.invoke(element)
    }

    /**
     * Should supply a new object. This will call the [onSupplyNew] runnable if it is not null. This
     * method is called when the pool is empty and an object is requested.
     */
    private fun supplyNew(): T {
        val element = supplier()
        GameLogger.debug(TAG, "Supplying new object: $element")
        onSupplyNew?.invoke(element)
        return element
    }
}
