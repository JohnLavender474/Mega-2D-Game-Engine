package com.engine.cullables

import java.util.function.Predicate

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

    /**
     * Constructor that takes lambdas for each of the cullable methods.
     *
     * @param shouldBeCulledFunction Lambda to be called when the object should be culled.
     * @param resetFunction Lambda to be called when the object should be reset.
     */
    constructor(
        shouldBeCulledFunction: Predicate<Float>,
        resetFunction: Runnable
    ) : this(
        shouldBeCulledFunction::test,
        resetFunction::run
    )

    override fun shouldBeCulled(delta: Float) = shouldBeCulledFunction(delta)

    override fun reset() = resetFunction()
}
