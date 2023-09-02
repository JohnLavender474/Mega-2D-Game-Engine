package com.engine.common.objects

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

  fun forEach(action: (E) -> Unit) = collection.forEach(action)

  fun <R> map(transform: (E) -> R) = collection.map(transform)

  fun filter(predicate: (E) -> Boolean) = collection.filter(predicate)
}
