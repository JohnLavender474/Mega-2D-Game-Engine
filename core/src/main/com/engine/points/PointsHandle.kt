package com.engine.points

/**
 * The points handle. Contains the stats and the listener for the stats.
 *
 * @param points The stats.
 * @param pointsListener The listener.
 */
data class PointsHandle(val points: Points, val pointsListener: (Points) -> Unit)
