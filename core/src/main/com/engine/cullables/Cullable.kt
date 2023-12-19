package com.engine.cullables

/**
 * A convenience implementation of [ICullable].
 *
 * @param shouldBeCulledFunction the function that determines if this object should be culled
 * @param resetFunction the function that resets this object
 */
class Cullable(
    private val shouldBeCulledFunction: (Float) -> Boolean,
    private val resetFunction: () -> Unit
) : ICullable {

  override fun shouldBeCulled(delta: Float) = shouldBeCulledFunction(delta)

  override fun reset() = resetFunction()
}
