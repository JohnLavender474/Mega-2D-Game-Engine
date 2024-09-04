package com.mega.game.engine.common.objects

/**
 * An immutable collection that wraps a [Collection] and prevents direct modification. Of course,
 * the underlying [Collection] can still be modified, but this class is useful for clearly
 * indicating when a collection should not be modified. (Although this distinction is already
 * enforced by the [Collection] interface, this class makes it more explicit.)
 *
 * @param E the type of elements in this collection
 */
class ImmutableCollection<E>(private val collection: Collection<E>) : Collection<E> {

    override val size: Int
        get() = collection.size

    override fun contains(element: E) = collection.contains(element)

    override fun containsAll(elements: Collection<E>) = collection.containsAll(elements)

    override fun isEmpty() = collection.isEmpty()

    override fun iterator(): ImmutableIterator<E> = ImmutableIterator(collection.iterator())

    override fun parallelStream() = collection.parallelStream()

    override fun spliterator() = collection.spliterator()

    override fun stream() = collection.stream()

    override fun equals(other: Any?) = collection == other

    override fun hashCode() = collection.hashCode()

    override fun toString() = "ImmutableCollection(collection=$collection)"
}
