package com.engine.cullables

import com.engine.common.interfaces.Containable

/**
 * A [Cullable] that will be culled if [containable] is not contained in [containerSupplier].
 *
 * @param containerSupplier a function that returns the container.
 * @param containable the containable.
 * @param T the type of the container.
 */
class CullableOnUncontained<T>(val containerSupplier: () -> T, val containable: Containable<T>) :
    Cullable {

  override fun shouldBeCulled() = !containable.isContainedIn(containerSupplier())
}
