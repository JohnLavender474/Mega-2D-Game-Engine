package com.engine.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.engine.common.interfaces.Drawable
import com.engine.common.objects.Array2D

/**
 * A 2D array of sprites. The sprites are positioned in a grid. The number of rows and columns are
 * specified in the constructor.
 *
 * @param model The model sprite that the sprites are copied on.
 * @param priority The priority of the sprites.
 * @param rows The number of rows.
 * @param cols The number of columns.
 */
class Sprite2dArray(model: Sprite, priority: Int, val rows: Int, val cols: Int) : Drawable {

  private val sprite2dArray = Array2D<Sprite>(rows, cols)

  init {
    for (i in 0 until rows) {
      for (j in 0 until cols) {
        sprite2dArray[i, j] = Sprite(model, priority)
      }
    }
    setPosition(model.x, model.y)
  }

  override fun draw(batch: Batch) = forEach { if (it.texture != null) it.draw(batch) }

  /**
   * Applies the specified action to each sprite in the array.
   *
   * @param action The action to apply to each sprite.
   */
  fun forEach(action: (Sprite) -> Unit) {
    for (i in 0 until rows) {
      for (j in 0 until cols) {
        sprite2dArray[i, j]?.let { action(it) }
      }
    }
  }

  /**
   * Sets the position of the sprites in the array.
   *
   * @param startX The x coordinate of the first sprite.
   * @param startY The y coordinate of the first sprite.
   */
  fun setPosition(startX: Float, startY: Float) {
    var x = startX
    var y = startY
    forEach {
      it.setPosition(x, y)
      x += it.width
      y += it.height
    }
  }

  /**
   * Translates the sprites in the array by the specified delta.
   *
   * @param x The x delta.
   * @param y The y delta.
   */
  fun translate(x: Float, y: Float) = forEach { it.translate(x, y) }
}
