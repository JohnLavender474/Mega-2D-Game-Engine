package com.engine.cullables

import com.engine.common.interfaces.Containable

/**
 * A [ICullable] that will be culled if [containable] is not contained in [containerSupplier].
 *
 * @param containerSupplier a function that returns the container.
 * @param containable the containable.
 * @param T the type of the container.
 */
class CullableOnUncontained<T>(val containerSupplier: () -> T, val containable: Containable<T>) :
    ICullable {

  override fun shouldBeCulled() = !containable.isContainedIn(containerSupplier())
}
