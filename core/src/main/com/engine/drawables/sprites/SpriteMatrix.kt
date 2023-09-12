package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.objects.Matrix

/**
 * A matrix of sprites. The sprites are positioned in a grid. The number of rows and columns are
 * specified in the constructor.
 *
 * @param model The model sprite that the sprites are copied on.
 * @param priority The priority of the sprites.
 * @param rows The number of rows.
 * @param columns The number of columns.
 */
class SpriteMatrix(
    model: TextureRegion,
    priority: Int,
    rows: Int,
    columns: Int,
    _x: Float,
    _y: Float
) : DrawableSprite, Matrix<IGameSprite>(rows, columns) {

  init {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        this[x, y] = GameSprite(model, priority)
      }
    }
    setPosition(_x, _y)
  }

  override fun draw(batch: Batch) = forEach { it.draw(batch) }

  /**
   * Applies the specified action to each sprite in the matrix.
   *
   * @param action The action to apply to each sprite.
   */
  fun forEach(action: (IGameSprite) -> Unit) {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        this[x, y]?.let { action(it) }
      }
    }
  }

  /**
   * Sets the position of the sprites in the array.
   *
   * @param startX The first coordinate of the first sprite.
   * @param startY The second coordinate of the first sprite.
   */
  fun setPosition(startX: Float, startY: Float) {
    var x = startX
    var y = startY
    forEach {
      it.setPosition(x, y)
      x += it.getWidth()
      y += it.getHeight()
    }
  }

  /**
   * Translates the sprites in the array by the specified delta.
   *
   * @param x The first delta.
   * @param y The second delta.
   */
  fun translate(x: Float, y: Float) = forEach { it.translate(x, y) }
}
