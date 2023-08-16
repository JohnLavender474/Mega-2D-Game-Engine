package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.extensions.PolylineExtensions.overlaps
import com.engine.common.shapes.extensions.RectangleExtensions.overlaps

object CircleExtensions {

    fun Circle.overlaps(r: Rectangle) = r.overlaps(this)

    fun Circle.overlaps(p: Polyline) = p.overlaps(this)

    // TODO: get points

}