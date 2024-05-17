package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Position

/**
 * Sets the position of this sprite to the specified position.
 *
 * @param position the position to set
 */
fun Sprite.setPosition(position: Vector2) = setPosition(position.x, position.y)

/**
 * Sets the position of this sprite to the specified position.
 *
 * @param p the position to set
 * @param pos the position to set this sprite to
 */
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

/**
 * Sets the position of this sprite to the specified position and offsets it by the specified
 * amount. The offset is applied after the position is set. The offset is applied to the x and y
 * coordinates.
 *
 * @param p the position to set
 * @param pos the position to set this sprite to
 * @param xOffset the x offset
 * @param yOffset the y offset
 */
fun Sprite.setPosition(p: Vector2, pos: Position, xOffset: Float, yOffset: Float) {
    setPosition(p, pos)
    translate(xOffset, yOffset)
}

/**
 * Sets the size of this sprite to the specified size.
 *
 * @param size the size to set
 */
fun Sprite.setSize(size: Float) = setSize(size, size)

/**
 * Sets the center of this sprite to the specified center.
 *
 * @param center the center to set
 */
fun Sprite.setCenter(center: Vector2) = setCenter(center.x, center.y)
