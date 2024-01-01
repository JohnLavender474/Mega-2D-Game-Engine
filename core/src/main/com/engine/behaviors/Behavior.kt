package com.engine.behaviors

/**
 * Implements an [AbstractBehavior] that can be used to create a behavior.
 *
 * @param evaluate The function that evaluates this [AbstractBehavior].
 * @param init The function that initializes this [AbstractBehavior].
 * @param act The function that performs the action of this [AbstractBehavior].
 * @param end The function that ends this [AbstractBehavior].
 */
class Behavior(
    private val evaluate: (delta: Float) -> Boolean,
    private val init: (() -> Unit)? = null,
    private val act: ((delta: Float) -> Unit)? = null,
    private val end: (() -> Unit)? = null
) : AbstractBehavior() {

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
