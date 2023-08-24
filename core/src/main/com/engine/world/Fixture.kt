package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.GameShape2D

data class Fixture(
    var shape: GameShape2D,
    var fixtureType: String,
    var active: Boolean = true,
    var attachedToBody: Boolean = true,
    var offsetFromBodyCenter: Vector2 = Vector2(),
    var userData: HashMap<String, Any?> = HashMap()
) {

  fun overlaps(other: GameShape2D) = shape.overlaps(other)

  fun overlaps(other: Fixture) = overlaps(other.shape)
}
