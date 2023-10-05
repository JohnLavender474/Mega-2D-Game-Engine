package com.engine.points

/**
 * The points handle. Contains the points and the listener for the points.
 *
 * @param points The [Points].
 * @param listener The listener.
 */
data class PointsHandle(val points: Points, val listener: (Points) -> Unit)
