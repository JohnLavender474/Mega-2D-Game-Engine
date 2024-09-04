package com.mega.game.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.Initializable
import java.util.function.Consumer
import java.util.function.Supplier

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

    /**
     * Creates a pool with the specified parameters.
     *
     * @param startAmount the initial amount of objects in the pool; default is 10
     * @param supplier the supplier of the objects
     * @param onSupplyNew a runnable that will be called when a new object is instantiated from the
     *  supplier
     * @param onFetch a runnable that will be called when an object is fetched from the pool
     * @param onPool a runnable that will be called when an object is pooled
     */
    constructor(
        startAmount: Int = 10,
        supplier: Supplier<T>,
        onSupplyNew: Consumer<T>? = null,
        onFetch: Consumer<T>? = null,
        onPool: Consumer<T>? = null,
    ) : this(
        startAmount,
        { supplier.get() },
        { onSupplyNew?.accept(it) },
        { onFetch?.accept(it) },
        { onPool?.accept(it) })

    /** Initialize the pool by supplying the initial amount of objects. */
    override fun init() {
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

    /**
     * Should supply a new object. This will call the [onSupplyNew] runnable if it is not null. This
     * method is called when the pool is empty and an object is requested.
     */
    private fun supplyNew(): T {
        val element = supplier()
        onSupplyNew?.invoke(element)
        return element
    }
}
