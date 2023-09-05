package com.engine.points

import com.engine.GameComponent

/**
 * The stats component. Contains all the stats for an entity.
 *
 * @param points The stats each mapped to a key.
 */
class PointsComponent(val points: MutableMap<String, PointsHandle> = HashMap()) : GameComponent
