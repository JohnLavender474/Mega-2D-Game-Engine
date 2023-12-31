package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.objects.Matrix
import com.engine.drawables.IDrawable
import com.engine.drawables.sorting.DrawingPriority

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
    val priority: DrawingPriority,
    val modelWidth: Float,
    val modelHeight: Float,
    val rows: Int,
    val columns: Int,
    val x: Float,
    val y: Float
)

/**
 * A matrix of sprites. The sprites are positioned in a grid. The number of rows and columns are
 * specified in the constructor. The sprites are copied from the model sprite. The sprites are
 * positioned in the grid starting from the bottom left corner.
 *
 * @param model The model sprite that the sprites are copied on.
 * @param modelWidth The width of the model sprite.
 * @param modelHeight The height of the model sprite.
 * @param rows The number of rows.
 * @param columns The number of columns.
 */
class SpriteMatrix(
    model: TextureRegion,
    priority: DrawingPriority,
    private val modelWidth: Float,
    private val modelHeight: Float,
    rows: Int,
    columns: Int
) : IDrawable<Batch>, Matrix<ISprite>(rows, columns) {

  init {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        val sprite = GameSprite(model, priority)
        sprite.setSize(modelWidth, modelHeight)
        sprite.setPosition(x * modelWidth, y * modelHeight)
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
  ) : this(
      params.model,
      params.priority,
      params.modelWidth,
      params.modelHeight,
      params.rows,
      params.columns)

  /**
   * Translates each sprite in the matrix.
   *
   * @param x The x coordinate to translate by.
   * @param y The y coordinate to translate by.
   */
  fun translate(x: Float, y: Float) = forEach { _, _, sprite ->
    (sprite as GameSprite).translate(x, y)
  }

  /** Resets the position of each sprite in the matrix. */
  fun resetPositions() {
    forEach { x, y, sprite -> (sprite as GameSprite).setPosition(x * modelWidth, y * modelHeight) }
  }

  /**
   * Draws each sprite in the matrix.
   *
   * @param drawer The batch to draw the sprites with.
   */
  override fun draw(drawer: Batch) = forEach { it.draw(drawer) }
}
