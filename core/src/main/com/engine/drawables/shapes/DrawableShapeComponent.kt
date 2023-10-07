package com.engine.drawables.shapes

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A [DrawableShapeComponent] is a [IGameComponent] that contains a list of [DrawableShapeHandle]s.
 * The [DrawableShapeHandle]s are the objects that can be drawn.
 *
 * @param shapes the [DrawableShapeHandle]s that can be drawn
 */
class DrawableShapeComponent(
    override val entity: IGameEntity,
    val shapes: Array<DrawableShapeHandle>
) : IGameComponent
