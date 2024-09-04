package com.mega.game.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.objects.Matrix
import com.mega.game.engine.drawables.IDrawable
import com.mega.game.engine.drawables.sorting.DrawingPriority

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
 * specified in the constructor. The size of each sprite is initialilly defined by [modelWidth] and
 * [modelHeight]. The sprites are positioned in the grid starting from the bottom left.
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
    private var modelWidth: Float,
    private var modelHeight: Float,
    rows: Int,
    columns: Int
) : IDrawable<Batch>, Matrix<GameSprite>(rows, columns) {

    init {
        for (x in 0 until columns) {
            for (y in 0 until rows) {
                val sprite = GameSprite(model, priority.copy())
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
        params.model, params.priority, params.modelWidth, params.modelHeight, params.rows, params.columns
    )

    /**
     * Translates each sprite in the matrix.
     *
     * @param x The x coordinate to translate by.
     * @param y The y coordinate to translate by.
     */
    fun translate(x: Float, y: Float) = forEach { _, _, sprite ->
        (sprite as GameSprite).translate(x, y)
    }

    /**
     * Sets the position of the matrix. The position of the first sprite is set to the specified
     * position. The position of the other sprites are set relative to the first sprite based on their
     * x and y coordinates and also the model width and height.
     *
     * @param startPosition The position of the first sprite.
     */
    fun setPosition(startPosition: Vector2) = setPosition(startPosition.x, startPosition.y)

    /**
     * Sets the position of the matrix. The position of the first sprite is set to the specified
     * position. The position of the other sprites are set relative to the first sprite based on their
     * x and y coordinates and also the model width and height.
     *
     * @param startX The x coordinate of the first sprite.
     * @param startY The y coordinate of the first sprite.
     */
    fun setPosition(startX: Float, startY: Float) {
        forEach { x, y, sprite ->
            sprite?.setPosition(startX + (x * modelWidth), startY + (y * modelHeight))
        }
    }

    override fun draw(drawer: Batch) = forEach { it.draw(drawer) }

    override fun toString(): String {
        return "SpriteMatrix(modelWidth=$modelWidth, modelHeight=$modelHeight, rows=$rows, columns=$columns, matrix=$matrixMap)"
    }
}
