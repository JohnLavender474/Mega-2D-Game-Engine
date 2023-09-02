package com.engine.common.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.collision.BoundingBox

fun BoundingBox.isInCamera(camera: Camera) =
    camera.frustum.boundsInFrustum(this)
