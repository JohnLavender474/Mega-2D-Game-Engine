package com.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.engine.common.shapes.toBoundingBox

fun Camera.overlaps(bounds: Rectangle) = frustum.boundsInFrustum(bounds.toBoundingBox())