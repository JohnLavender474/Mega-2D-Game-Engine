package com.engine.systems

import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity
import kotlin.reflect.KClass

/**
 * An [IntervalGameSystem] is a [GameSystem] that processes [IGameEntity]s at a fixed interval. It
 * contains a [componentMask] which determines which [IGameEntity]s it processes.
 *
 * @param intervalSupplier a lambda that supplies the interval in seconds between each update
 * @param componentMask the [Collection] of [KClass]es of [IGameComponent]s that this
 */
abstract class IntervalGameSystem(
    var intervalSupplier: () -> Float,
    componentMask: Iterable<KClass<out IGameComponent>>
) : GameSystem(componentMask) {

    /**
     * @param interval the interval in seconds between each update
     * @param componentMask the [Collection] of [KClass]es of [IGameComponent]s that this
     * @see IntervalGameSystem(intervalSupplier: () -> Float, componentMask: Collection<KClass<out
     *   IGameComponent>>)
     */
    constructor(
        interval: Float,
        vararg componentMask: KClass<out IGameComponent>
    ) : this({ interval }, componentMask.toList())

    var accumulator = 0f
        private set

    override fun update(delta: Float) {
        accumulator += delta
        while (accumulator >= intervalSupplier()) {
            accumulator -= intervalSupplier()
            super.update(intervalSupplier())
        }
    }
}
