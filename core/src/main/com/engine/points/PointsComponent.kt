package com.engine.points

import com.engine.GameComponent

/**
 * The stats component. Contains all the stats for an entity.
 *
 * @param pointsHandles The points handles.
 */
class PointsComponent(val pointsHandles: MutableList<PointsHandle> = ArrayList()) : GameComponent
