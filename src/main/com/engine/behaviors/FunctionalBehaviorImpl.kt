package com.engine.behaviors

import java.util.function.Consumer
import java.util.function.Function

/**
 * Convenience class which extends [AbstractBehaviorImpl] that can be used to create a behavior via lambda functions.
 *
 * @param evaluate The function that evaluates this [AbstractBehaviorImpl].
 * @param init The function that initializes this [AbstractBehaviorImpl].
 * @param act The function that performs the action of this [AbstractBehaviorImpl].
 * @param end The function that ends this [AbstractBehaviorImpl].
 */
class FunctionalBehaviorImpl(
    private val evaluate: (delta: Float) -> Boolean,
    private val init: (() -> Unit)? = null,
    private val act: ((delta: Float) -> Unit)? = null,
    private val end: (() -> Unit)? = null
) : AbstractBehaviorImpl() {

    /**
     * Convenience constructor that accepts Java [Function] and [Consumer] instances.
     *
     * @param evaluate The function that evaluates this [AbstractBehaviorImpl].
     * @param init The function that initializes this [AbstractBehaviorImpl].
     * @param act The function that performs the action of this [AbstractBehaviorImpl].
     * @param end The function that ends this [AbstractBehaviorImpl].
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
