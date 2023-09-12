package com.engine.drawables.shapes

import com.engine.GameComponent

/**
 * A [DrawableShapeComponent] is a [GameComponent] that contains a list of [DrawableShapeHandle]s.
 * The [DrawableShapeHandle]s are the objects that can be drawn.
 *
 * @param shapes the [DrawableShapeHandle]s that can be drawn
 */
class DrawableShapeComponent(val shapes: ArrayList<DrawableShapeHandle>) : GameComponent
