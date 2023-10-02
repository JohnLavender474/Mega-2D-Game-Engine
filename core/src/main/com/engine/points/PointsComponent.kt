package com.engine.points

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent

/**
 * The stats component. Contains all the stats for an entity.
 *
 * @param pointsHandles The points handles.
 */
class PointsComponent(val pointsHandles: Array<PointsHandle> = Array()) : IGameComponent
