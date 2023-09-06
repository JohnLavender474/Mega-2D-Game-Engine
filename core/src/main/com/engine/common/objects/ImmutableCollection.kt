package com.engine.common.objects

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

  /**
   * Returns whether this collection contains the given element.
   *
   * @param element the element to check
   * @return whether this collection contains the given element
   */
  override fun contains(element: E) = collection.contains(element)

  /**
   * Returns whether this collection contains all the given elements.
   *
   * @param elements the elements to check
   * @return whether this collection contains all the given elements
   */
  override fun containsAll(elements: Collection<E>) = collection.containsAll(elements)

  /**
   * Returns whether this collection is empty.
   *
   * @return whether this collection is empty
   */
  override fun isEmpty() = collection.isEmpty()

  /**
   * Returns an iterator over the elements in this collection.
   *
   * @return an iterator over the elements in this collection
   */
  override fun iterator(): ImmutableIterator<E> = ImmutableIterator(collection.iterator())

  /**
   * Returns a parallel stream of the elements in this collection.
   *
   * @return a parallel stream of the elements in this collection
   */
  override fun parallelStream() = collection.parallelStream()

  /**
   * Returns a spliterator over the elements in this collection.
   *
   * @return a spliterator over the elements in this collection
   */
  override fun spliterator() = collection.spliterator()

  /**
   * Returns a stream of the elements in this collection.
   *
   * @return a stream of the elements in this collection
   */
  override fun stream() = collection.stream()
}
