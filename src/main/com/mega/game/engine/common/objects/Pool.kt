package com.mega.game.engine.common.objects

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.Initializable

open class Pool<T>(
    var supplier: () -> T,
    private var startAmount: Int = 10,
    var onSupplyNew: ((T) -> Unit)? = null,
    var onFetch: ((T) -> Unit)? = null,
    var onPool: ((T) -> Unit)? = null
) : Initializable {

    companion object {
        const val TAG = "Pool"
    }

    private var initialized = false
    private val queue = Array<T>()

    override fun init() {
        (0 until startAmount).forEach { free(supplyNew()) }
        initialized = true
    }

    fun fetch(): T {
        if (!initialized) init()
        val element = if (queue.isEmpty) supplyNew() else queue.pop()
        onFetch?.invoke(element)
        return element
    }

    fun free(element: T) {
        queue.add(element)
        onPool?.invoke(element)
    }

    fun clear() = queue.clear()

    private fun supplyNew(): T {
        val element = supplier()
        onSupplyNew?.invoke(element)
        return element
    }
}
