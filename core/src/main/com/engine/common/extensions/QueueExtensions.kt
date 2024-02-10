package com.engine.common.extensions

import com.badlogic.gdx.utils.Queue

/**
 * Checks if this queue contains the specified element.
 *
 * @param element the element to check for
 * @return true if this queue contains the element, false otherwise
 */
fun <T> Queue<T>.has(element: T): Boolean {
    for (i in 0 until size) {
        if (get(i) == element) return true
    }
    return false
}
