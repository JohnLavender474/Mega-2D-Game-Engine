package com.engine.common.shapes

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.engine.common.extensions.isInCamera

fun Rectangle.toBoundingBox() = BoundingBox(Vector3(x, y, 0f), Vector3(x + width, y + height, 0f))

fun Rectangle.isInCamera(camera: Camera) = toBoundingBox().isInCamera(camera)
