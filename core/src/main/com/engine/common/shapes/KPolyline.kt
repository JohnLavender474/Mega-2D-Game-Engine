package com.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.extensions.PolylineExtensions.getAsLines
import kotlin.math.pow

class KPolyline : Polyline, KShape2D {

  constructor() : super()

  constructor(vertices: FloatArray) : super(vertices)

  override fun getCenter(): Vector2 = boundingRectangle.getCenter(Vector2())

  override fun setCenterX(centerX: Float) {
    val currCenterX = boundingRectangle.x + boundingRectangle.width / 2f
    val translateX = centerX - currCenterX
    translate(translateX, 0f)
  }

  override fun setCenterY(centerY: Float) {
    val currCenterY = boundingRectangle.y + boundingRectangle.height / 2f
    val translateY = centerY - currCenterY
    translate(0f, translateY)
  }

  override fun getMaxX() = boundingRectangle.x + boundingRectangle.width

  override fun getMaxY() = boundingRectangle.y + boundingRectangle.height

  override fun setMaxX(maxX: Float) {
    val currX = boundingRectangle.x
    val translateX = maxX - currX - boundingRectangle.width
    translate(translateX, 0f)
  }

  override fun setMaxY(maxY: Float) {
    val currY = boundingRectangle.y
    val translateY = maxY - currY - boundingRectangle.height
    translate(0f, translateY)
  }

  override fun overlaps(other: KShape2D) =
      when (other) {
        is KRectangle ->
            getAsLines().any {
              Intersector.intersectSegmentRectangle(
                  it.point1.x, it.point1.y, it.point2.x, it.point2.y, other)
            }
        is KCircle ->
            getAsLines().any {
              Intersector.intersectSegmentCircle(
                  it.point1, it.point2, other.getCenter(), other.radius().pow(2))
            }
        is KLine ->
            getAsLines().any {
              Intersector.intersectLines(
                  it.point1, it.point2, other.point1, other.point2, Vector2())
            }
        is KPolyline ->
            getAsLines().any { pLine ->
              other.getAsLines().any {
                Intersector.intersectLines(
                    it.point1, it.point2, pLine.point1, pLine.point2, Vector2())
              }
            }
        else -> false
      }

  override fun positionOnPoint(point: Vector2, position: Position) {
    when (position) {
      Position.TOP_LEFT -> {
        val translateX = point.x - boundingRectangle.x
        translate(translateX, 0f)
        setMaxY(point.y)
      }
      Position.TOP_CENTER -> {
        val translateX = point.x - getCenter().x
        translate(translateX, 0f)
        setMaxY(point.y)
      }
      Position.TOP_RIGHT -> {
        val translateX = point.x - getMaxX()
        translate(translateX, 0f)
        setMaxY(point.y)
      }
      Position.CENTER_LEFT -> {
        val translateX = point.x - boundingRectangle.x
        translate(translateX, 0f)
        setCenterY(point.y)
      }
      Position.CENTER -> {
        setCenterX(point.x)
        setCenterY(point.y)
      }
      Position.CENTER_RIGHT -> {
        val translateX = point.x - getMaxX()
        translate(translateX, 0f)
        setCenterY(point.y)
      }
      Position.BOTTOM_LEFT -> {
        val translateX = point.x - boundingRectangle.x
        val translateY = point.y - boundingRectangle.y
        translate(translateX, translateY)
      }
      Position.BOTTOM_CENTER -> {
        val translateX = point.x - getCenter().x
        val translateY = point.y - boundingRectangle.y
        translate(translateX, translateY)
      }
      Position.BOTTOM_RIGHT -> {
        val translateX = point.x - getMaxX()
        val translateY = point.y - boundingRectangle.y
        translate(translateX, translateY)
      }
    }
  }

  override fun getPositionPoint(position: Position) =
      when (position) {
        Position.TOP_LEFT -> Vector2(boundingRectangle.x, getMaxY())
        Position.TOP_CENTER -> Vector2(getCenter().x, getMaxY())
        Position.TOP_RIGHT -> Vector2(getMaxX(), getMaxY())
        Position.CENTER_LEFT -> Vector2(boundingRectangle.x, getCenter().y)
        Position.CENTER -> getCenter()
        Position.CENTER_RIGHT -> Vector2(getMaxX(), getCenter().y)
        Position.BOTTOM_LEFT -> Vector2(boundingRectangle.x, boundingRectangle.y)
        Position.BOTTOM_CENTER -> Vector2(getCenter().x, boundingRectangle.y)
        Position.BOTTOM_RIGHT -> Vector2(getMaxX(), boundingRectangle.y)
      }

  override fun contains(point: Vector2): Boolean = contains(point)
}
