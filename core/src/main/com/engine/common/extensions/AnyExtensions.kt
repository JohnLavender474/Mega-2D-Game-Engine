package com.engine.common.extensions
/**
 * Checks if the current object equals any of the provided varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the current object equals any of the provided varargs or within the Iterable,
 *   false otherwise
 */
fun Any.equalsAny(vararg objects: Any): Boolean = objects.any { this == it }

/**
 * Checks if the current object equals all the provided varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the current object equals all the provided varargs or within the Iterable,
 *   false otherwise
 */
fun Any.equalsAll(objects: Iterable<Any>): Boolean = objects.all { this == it }

/**
 * Checks if the current object equals none of the provided varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the current object equals none of the provided varargs or within the Iterable,
 *   false otherwise
 */
fun Any.equalsNone(vararg objects: Any): Boolean = objects.none { this == it }

/**
 * Counts the number of provided varargs or within the Iterable that the current object equals.
 *
 * @param objects the varargs or Iterable to check
 * @return the number of provided varargs or within the Iterable that the current object equals
 */
fun Any.equalsCount(objects: Iterable<Any>): Int = objects.count { this == it }

/**
 * Checks if the hash code of the current object equals any of the hash codes from the provided
 * varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the hash code of the current object equals any of the hash codes from the
 *   provided
 */
fun Any.hashCodeAny(vararg objects: Any): Boolean = objects.any { this.hashCode() == it.hashCode() }

/**
 * Checks if the hash code of the current object equals all the hash codes from the provided
 * varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the hash code of the current object equals all the hash codes from the
 *   provided
 */
fun Any.hashCodeAll(objects: Iterable<Any>): Boolean =
    objects.all { this.hashCode() == it.hashCode() }

/**
 * Checks if the hash code of the current object equals none of the hash codes from the provided
 * varargs or within the Iterable.
 *
 * @param objects the varargs or Iterable to check
 * @return true if the hash code of the current object equals none of the hash codes from the
 *   provided
 */
fun Any.hashCodeNone(vararg objects: Any): Boolean =
    objects.none { this.hashCode() == it.hashCode() }

/**
 * Counts the number of provided varargs or within the Iterable that the hash code of the current
 * object equals.
 *
 * @param objects the varargs or Iterable to check
 * @return the number of provided varargs or within the Iterable that the hash code of the current
 */
fun Any.hashCodeCount(objects: Iterable<Any>): Int =
    objects.count { this.hashCode() == it.hashCode() }
