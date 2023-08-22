package com.engine.common.shapes.extensions

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position
import com.engine.common.shapes.Line
import com.engine.common.shapes.extensions.PolylineExtensions.overlaps
import com.engine.common.shapes.extensions.RectangleExtensions.overlaps
import com.engine.common.shapes.utils.IntersectorUtils

object CircleExtensions {

  fun Circle.overlaps(r: Rectangle) = r.overlaps(this)

  fun Circle.overlaps(p: Polyline) = p.overlaps(this)

  fun Circle.overlaps(l: Line) = l.overlaps(this)

  fun Circle.getBoundingRectangle(): Rectangle = Rectangle().setSize(radius * 2f).setCenter(x, y)

  fun Circle.getCenter(): Vector2 = Vector2(x, y)

  fun Circle.setCenterX(centerX: Float) {
    x = centerX
  }

  fun Circle.setCenterY(centerY: Float) {
    y = centerY
  }

  fun Circle.getMaxX() = x + radius

  fun Circle.getMaxY() = y + radius

  fun Circle.setMaxX(maxX: Float) {
    x = maxX - radius
  }

  fun Circle.setMaxY(maxY: Float) {
    y = maxY - radius
  }

  fun Circle.translate(translateX: Float, translateY: Float) {
    x += translateX
    y += translateY
  }

  fun Circle.positionOnPoint(point: Vector2, position: Position) {
    when (position) {
      Position.TOP_LEFT -> {
        x = point.x + radius
        y = point.y - radius
      }
      Position.TOP_CENTER -> {
        x = point.x
        y = point.y - radius
      }
      Position.TOP_RIGHT -> {
        x = point.x - radius
        y = point.y - radius
      }
      Position.CENTER_LEFT -> {
        x = point.x + radius
        y = point.y
      }
      Position.CENTER -> {
        x = point.x
        y = point.y
      }
      Position.CENTER_RIGHT -> {
        x = point.x - radius
        y = point.y
      }
      Position.BOTTOM_LEFT -> {
        x = point.x + radius
        y = point.y + radius
      }
      Position.BOTTOM_CENTER -> {
        x = point.x
        y = point.y + radius
      }
      Position.BOTTOM_RIGHT -> {
        x = point.x - radius
        y = point.y + radius
      }
    }
  }

  fun Circle.getPositionPoint(position: Position) =
      when (position) {
        Position.TOP_LEFT -> Vector2(x - radius, y + radius)
        Position.TOP_CENTER -> Vector2(x, y + radius)
        Position.TOP_RIGHT -> Vector2(x + radius, y + radius)
        Position.CENTER_LEFT -> Vector2(x - radius, y)
        Position.CENTER -> Vector2(x, y)
        Position.CENTER_RIGHT -> Vector2(x + radius, y)
        Position.BOTTOM_LEFT -> Vector2(x - radius, y - radius)
        Position.BOTTOM_CENTER -> Vector2(x, y - radius)
        Position.BOTTOM_RIGHT -> Vector2(x + radius, y - radius)
      }
}
