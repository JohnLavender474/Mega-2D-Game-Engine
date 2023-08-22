package com.engine.world

import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2

data class Fixture(
    var shape: Shape2D,
    var fixtureType: String,
    var attachedToBody: Boolean = true,
    var offsetFromBodyCenter: Vector2 = Vector2(),
    var userData: HashMap<String, Any?> = HashMap()
)
