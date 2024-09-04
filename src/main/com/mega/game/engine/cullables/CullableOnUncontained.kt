package com.mega.game.engine.cullables

import com.mega.game.engine.common.interfaces.IContainable
import java.util.function.Supplier

/**
 * A [ICullable] that will be culled if [containable] is not contained in [containerSupplier].
 *
 * @param containerSupplier a function that returns the container.
 * @param containable the containable.
 * @param T the type of the container.
 */
class CullableOnUncontained<T>(
    val containerSupplier: () -> T,
    val containable: IContainable<T>,
    var timeToCull: Float = 0f
) : com.mega.game.engine.cullables.ICullable {

    private var timeUncontained = 0f
    private var shouldBeCulled = false

    /**
     * Constructor that takes a lambda for the containerSupplier function.
     *
     * @param containerSupplier Lambda to be called when the container is needed.
     * @param containable The object that should be contained.
     * @param timeToCull The time to wait before culling.
     */
    constructor(
        containerSupplier: Supplier<T>,
        containable: IContainable<T>,
        timeToCull: Float = 0f
    ) : this(
        containerSupplier::get,
        containable,
        timeToCull
    )

    override fun shouldBeCulled(delta: Float): Boolean {
        if (shouldBeCulled) return true

        val uncontained = !containable.isContainedIn(containerSupplier())
        if (uncontained) {
            timeUncontained += delta
            if (timeUncontained >= timeToCull) {
                timeUncontained = 0f
                shouldBeCulled = true
            }
        } else timeUncontained = 0f
        return shouldBeCulled
    }

    override fun reset() {
        super.reset()
        shouldBeCulled = false
    }

    override fun toString() = "CullableOnUncontained"
}
