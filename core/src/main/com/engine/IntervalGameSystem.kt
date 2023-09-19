package com.engine

import kotlin.reflect.KClass

/**
 * An [IntervalGameSystem] is a [GameSystem] that processes [GameEntity]s at a fixed interval. It
 * contains a [componentMask] which determines which [GameEntity]s it processes.
 *
 * @param intervalSupplier a lambda that supplies the interval in seconds between each update
 * @param componentMask the [Collection] of [KClass]es of [GameComponent]s that this
 */
abstract class IntervalGameSystem(
    var intervalSupplier: () -> Float,
    componentMask: Collection<KClass<out GameComponent>>
) : GameSystem(componentMask) {

  /**
   * @param interval the interval in seconds between each update
   * @param componentMask the [Collection] of [KClass]es of [GameComponent]s that this
   * @see IntervalGameSystem(intervalSupplier: () -> Float, componentMask: Collection<KClass<out
   *   GameComponent>>)
   */
  constructor(
      interval: Float,
      vararg componentMask: KClass<out GameComponent>
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
