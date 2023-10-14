package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.objects.Matrix

/**
 * Convenience class for creating a [SpriteMatrix] with the specified parameters.
 *
 * @param model The model sprite that the sprites are copied on.
 * @param modelWidth The width of the model sprite.
 * @param modelHeight The height of the model sprite.
 * @param rows The number of rows.
 * @param columns The number of columns.
 * @param x The x coordinate of the first sprite.
 * @param y The y coordinate of the first sprite.
 * @param priority The priority of the sprites.
 */
data class SpriteMatrixParams(
    val model: TextureRegion,
    val modelWidth: Float,
    val modelHeight: Float,
    val rows: Int,
    val columns: Int,
    val x: Float,
    val y: Float,
    val priority: Int = 0
)

/**
 * A matrix of sprites. The sprites are positioned in a grid. The number of rows and columns are
 * specified in the constructor. The sprites are copied from the model sprite. The sprites are
 * positioned in the grid starting from the top left corner.
 *
 * @param model The model sprite that the sprites are copied on.
 * @param modelWidth The width of the model sprite.
 * @param modelHeight The height of the model sprite.
 * @param rows The number of rows.
 * @param columns The number of columns.
 */
class SpriteMatrix(
    model: TextureRegion,
    modelWidth: Float,
    modelHeight: Float,
    rows: Int,
    columns: Int
) : ISprite, Matrix<ISprite>(rows, columns) {

  init {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        val sprite = GameSprite(model)
        sprite.setSize(modelWidth, modelHeight)
        this[x, y] = sprite
      }
    }
  }

  /**
   * Creates a [SpriteMatrix] with the specified parameters.
   *
   * @param params The parameters to use.
   */
  constructor(
      params: SpriteMatrixParams
  ) : this(params.model, params.modelWidth, params.modelHeight, params.rows, params.columns)

  /**
   * Draws each sprite in the matrix.
   *
   * @param drawer The batch to draw the sprites with.
   */
  override fun draw(drawer: Batch) = forEach { it.draw(drawer) }

  /**
   * Sets the region of each sprite in the matrix.
   *
   * @param _region The region to set.
   */
  override fun setRegion(_region: TextureRegion) = forEach { it.setRegion(_region) }

  /**
   * Sets the texture of each sprite in the matrix.
   *
   * @param _texture The texture to set.
   */
  override fun setTexture(_texture: Texture) = forEach { it.setTexture(_texture) }

  /**
   * Applies the specified action to each sprite in the matrix.
   *
   * @param action The action to apply to each sprite.
   */
  fun forEach(action: (ISprite) -> Unit) {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        this[x, y]?.let { action(it) }
      }
    }
  }

  /**
   * Sets the position of the sprites in the array.
   *
   * @param x The first coordinate of the first sprite.
   * @param y The second coordinate of the first sprite.
   */
  override fun setPosition(x: Float, y: Float) {
    var _x = x
    var _y = y
    forEach {
      it.setPosition(_x, _y)
      _x += it.getWidth()
      _y += it.getHeight()
    }
  }

  /**
   * Gets the x coordinate of the first sprite in the matrix.
   *
   * @return The x coordinate of the first sprite in the matrix.
   */
  override fun getX() = this[0, 0]?.getX() ?: 0f

  /** Gets the y coordinate of the first sprite in the matrix. */
  override fun getY() = this[0, 0]?.getY() ?: 0f

  /**
   * Translates the sprites in the matrix.
   *
   * @param x The x to translate by.
   * @param y The y to translate by.
   */
  override fun translate(x: Float, y: Float) = forEach { it.translate(x, y) }

  /**
   * Returns the width of the first row in the matrix.
   *
   * @return The width of the first row in the matrix.
   */
  override fun getWidth(): Float {
    var width = 0f
    for (x in 0 until columns) {
      width += this[x, 0]?.getWidth()?.toInt() ?: 0
    }
    return width
  }

  /**
   * Returns the height of the first column in the matrix.
   *
   * @return The height of the first column in the matrix.
   */
  override fun getHeight(): Float {
    var height = 0f
    for (y in 0 until rows) {
      height += this[0, y]?.getHeight()?.toInt() ?: 0
    }
    return height
  }

  /**
   * Sets the width of each sprite in the matrix.
   *
   * @param width The width to set.
   */
  override fun setWidth(width: Float) = forEach { it.setWidth(width) }

  /**
   * Sets the height of each sprite in the matrix.
   *
   * @param height The height to set.
   */
  override fun setHeight(height: Float) = forEach { it.setHeight(height) }
}
