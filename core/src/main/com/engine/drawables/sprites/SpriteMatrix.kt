package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.objects.Matrix
import com.engine.common.objects.Properties
import com.engine.drawables.sorting.DrawingPriority
import com.engine.drawables.IDrawable

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
    priority: DrawingPriority,
    modelWidth: Float,
    modelHeight: Float,
    rows: Int,
    columns: Int,
    override val properties: Properties = Properties()
) : IDrawable<Batch>, Matrix<ISprite>(rows, columns) {

  init {
    for (x in 0 until columns) {
      for (y in 0 until rows) {
        val sprite = GameSprite(model, priority)
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
  ) : this(
      params.model,
      params.priority,
      params.modelWidth,
      params.modelHeight,
      params.rows,
      params.columns)

  /**
   * Draws each sprite in the matrix.
   *
   * @param drawer The batch to draw the sprites with.
   */
  override fun draw(drawer: Batch) = forEach { it.draw(drawer) }
}
