package com.engine.common.extensions

import com.engine.common.objects.ImmutableCollection

fun <T> Collection<T>.toImmutableCollection() = ImmutableCollection(this)
