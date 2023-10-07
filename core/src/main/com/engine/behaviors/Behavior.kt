package com.engine.behaviors

/**
 * Implements an [IBehavior] that can be used to create a behavior.
 *
 * @param evaluate The function that evaluates this [IBehavior].
 * @param init The function that initializes this [IBehavior].
 * @param act The function that performs the action of this [IBehavior].
 * @param end The function that ends this [IBehavior].
 */
class Behavior(
    private val evaluate: (delta: Float) -> Boolean,
    private val init: (() -> Unit)? = null,
    private val act: ((delta: Float) -> Unit)? = null,
    private val end: (() -> Unit)? = null
) : IBehavior() {

  override fun evaluate(delta: Float) = evaluate.invoke(delta)

  override fun init() {
    init?.invoke()
  }

  override fun act(delta: Float) {
    act?.invoke(delta)
  }

  override fun end() {
    end?.invoke()
  }
}
