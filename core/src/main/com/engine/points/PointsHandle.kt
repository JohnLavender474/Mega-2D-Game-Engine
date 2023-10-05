package com.engine.points

import com.engine.common.interfaces.Resettable

/**
 * The points handle. Contains the points and the listener for the points.
 *
 * @param points The [Points].
 * @param listener The listener.
 * @param onReset The function to call on reset.
 */
data class PointsHandle(
    val points: Points,
    val listener: (Points) -> Unit,
    val onReset: ((PointsHandle) -> Unit)? = null
) : Resettable {

  override fun reset() {
    onReset?.invoke(this)
  }
}
