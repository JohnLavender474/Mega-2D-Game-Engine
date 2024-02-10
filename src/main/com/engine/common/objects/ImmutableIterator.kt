package com.engine.common.objects

/** An iterator that cannot be modified. */
class ImmutableIterator<E>(private val iterator: Iterator<E>) : Iterator<E> {

    override fun hasNext() = iterator.hasNext()

    override fun next() = iterator.next()
}
