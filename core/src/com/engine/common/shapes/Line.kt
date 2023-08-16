package com.engine.common.shapes

import com.badlogic.gdx.math.Vector2

data class Line(val point1: Vector2, val point2: Vector2) {

    constructor(v: Array<Float>) : this(Vector2(v[0], v[1]), Vector2(v[2], v[3]))

}
