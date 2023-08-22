package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.*
import com.engine.common.enums.Position
import com.engine.common.shapes.Line
import com.engine.common.shapes.extensions.CircleExtensions.getCenter
import kotlin.math.pow

object PolylineExtensions {

  fun Polyline.getAsLines(): List<Line> {
    val v = transformedVertices
    val lines = ArrayList<Line>()
    for (i in 0..(v.size - 3) step 2) {
      val p1 = Vector2(v[i], v[i + 1])
      val p2 = Vector2(v[i + 2], v[i + 3])
      lines.add(Line(p1, p2))
    }
    return lines
  }

  fun Polyline.getAsLine() = getAsLines()

  fun Polyline.getCenter(): Vector2 = boundingRectangle.getCenter(Vector2())

  fun Polyline.setCenterX(centerX: Float) {
    val currCenterX = boundingRectangle.x + boundingRectangle.width / 2f
    val translateX = centerX - currCenterX
    translate(translateX, 0f)
  }

  fun Polyline.setCenterY(centerY: Float) {
    val currCenterY = boundingRectangle.y + boundingRectangle.height / 2f
    val translateY = centerY - currCenterY
    translate(0f, translateY)
  }

  fun Polyline.getMaxX() = boundingRectangle.x + boundingRectangle.width

  fun Polyline.getMaxY() = boundingRectangle.y + boundingRectangle.height

  fun Polyline.setMaxX(maxX: Float) {
    val currX = boundingRectangle.x
    val translateX = maxX - currX - boundingRectangle.width
    translate(translateX, 0f)
  }

  fun Polyline.setMaxY(maxY: Float) {
    val currY = boundingRectangle.y
    val translateY = maxY - currY - boundingRectangle.height
    translate(0f, translateY)
  }

  fun Polyline.overlaps(r: Rectangle) =
      getAsLines().any {
        Intersector.intersectSegmentRectangle(it.point1.x, it.point1.y, it.point2.x, it.point2.y, r)
      }

  fun Polyline.overlaps(c: Circle) =
      getAsLines().any {
        Intersector.intersectSegmentCircle(it.point1, it.point2, c.getCenter(), c.radius.pow(2))
      }

  fun Polyline.overlaps(l: Line) =
      getAsLines().any {
        Intersector.intersectLines(it.point1, it.point2, l.point1, l.point2, Vector2())
      }

  fun Polyline.overlaps(p: Polyline) =
      getAsLines().any { pLine ->
        p.getAsLines().any {
          Intersector.intersectLines(it.point1, it.point2, pLine.point1, pLine.point2, Vector2())
        }
      }

  fun Polyline.positionOnPoint(point: Vector2, position: Position) {
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

  fun Polyline.getPositionPoint(position: Position) =
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
}
