package com.engine.common.extensions

import com.engine.common.objects.ImmutableCollection

/**
 * Converts this [Collection] to an [ImmutableCollection].
 *
 * @return an [ImmutableCollection] with the same elements as this [Collection]
 */
fun <T> Collection<T>.toImmutableCollection() = ImmutableCollection(this)
