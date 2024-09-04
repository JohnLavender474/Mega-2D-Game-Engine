package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Polygon

/**
 * Converts this [Polygon] to a [GamePolygon].
 *
 * @return A [GamePolygon] with the same vertices and fields as this [Polygon].
 */
fun Polygon.toGamePolygon() = GamePolygon(this)