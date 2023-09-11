package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

fun Sprite.setPosition(p: Vector2, pos: Position) {
  when (pos) {
    Position.BOTTOM_LEFT -> setPosition(p.x, p.y)
    Position.BOTTOM_CENTER -> setPosition(p.x - width / 2f, p.y)
    Position.BOTTOM_RIGHT -> setPosition(p.x - width, p.y)
    Position.CENTER_LEFT -> {
      setCenter(p.x, p.y)
      x += width / 2f
    }
    Position.CENTER -> setCenter(p.x, p.y)
    Position.CENTER_RIGHT -> {
      setCenter(p.x, p.y)
      x -= width / 2f
    }
    Position.TOP_LEFT -> setPosition(p.x, p.y - height)
    Position.TOP_CENTER -> {
      setCenter(p.x, p.y)
      y -= height / 2f
    }
    Position.TOP_RIGHT -> setPosition(p.x - width, p.y - height)
  }
}

fun Sprite.setPosition(p: Vector2, pos: Position, xOffset: Float, yOffset: Float) {
  setPosition(p, pos)
  translate(xOffset, yOffset)
}
