package com.engine.behaviors

import java.util.function.Consumer
import java.util.function.Function

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

    /**
     * Convenience constructor that accepts Java [Function] and [Consumer] instances.
     *
     * @param evaluate The function that evaluates this [AbstractBehavior].
     * @param init The function that initializes this [AbstractBehavior].
     * @param act The function that performs the action of this [AbstractBehavior].
     * @param end The function that ends this [AbstractBehavior].
     */
    constructor(
        evaluate: Function<Float, Boolean>, init: Runnable? = null, act: Consumer<Float>? = null, end: Runnable? = null
    ) : this({ delta -> evaluate.apply(delta) }, { init?.run() }, { delta -> act?.accept(delta) }, { end?.run() })

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
