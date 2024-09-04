package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.utils.Array

/**
 * Converts this [Polyline] to an array of [GameLine]s. Each line is created from two consecutive vertices
 * in the polyline. If the polyline has an odd number of vertices, the last vertex is ignored. If the polyline
 * has less than two vertices, an empty array is returned.
 *
 * @return An array of [GameLine]s with the same vertices as this [Polyline].
 */
fun Polyline.toGameLines(): Array<GameLine> {
    val lines = Array<GameLine>()
    for (i in 0 until this.vertices.size - 2 step 2) {
        val line = GameLine(this.vertices[i], this.vertices[i + 1], this.vertices[i + 2], this.vertices[i + 3])
        lines.add(line)
    }
    return lines
}