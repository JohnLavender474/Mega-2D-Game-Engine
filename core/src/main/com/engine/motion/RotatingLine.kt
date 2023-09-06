package com.engine.motion

import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Vector2

class RotatingLine(
    origin: Vector2,
    radius: Float,
    var speed: Float,
    var degreesOnReset: Float = 0f
) : Motion {

  private val vertices = FloatArray(4)
  private val polyline = Polyline()
  private var degrees = degreesOnReset

  init {
    val endPoint = origin.cpy().add(radius, 0f)
    polyline.vertices = vertices
    polyline.rotation = degreesOnReset
    set(origin, endPoint)
  }

  override fun getMotionValue() = getEndPoint()

  override fun update(delta: Float) {
    degrees += speed * delta
    polyline.rotation = degreesOnReset
  }

  override fun reset() {
    degrees = degreesOnReset
  }

  fun set(origin: Vector2, endPoint: Vector2) {
    setOrigin(origin)
    setEndPoint(endPoint)
  }

  fun getOrigin() = Vector2(polyline.originX, polyline.originY)

  fun setOrigin(origin: Vector2) = setOrigin(origin.x, origin.y)

  fun setOrigin(x: Float, y: Float) {
    vertices[0] = x
    vertices[1] = y
  }

  fun getEndPoint() = Vector2(polyline.transformedVertices[2], polyline.transformedVertices[3])

  fun setEndPoint(endPoint: Vector2) = setEndPoint(endPoint.x, endPoint.y)

  fun setEndPoint(x: Float, y: Float) {
    vertices[2] = x
    vertices[3] = y
  }

  fun getScaledPosition(scalar: Float): Vector2 {
    val endPoint = getEndPoint()
    val x = polyline.originX + (endPoint.x - polyline.originX) * scalar
    val y = polyline.originY + (endPoint.y - polyline.originY) * scalar
    return Vector2(x, y)
  }

  fun translate(x: Float, y: Float) {
    polyline.setOrigin(polyline.originX + x, polyline.originY + y)
  }
}
