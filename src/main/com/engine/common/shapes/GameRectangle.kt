package com.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.FloatArray
import com.engine.common.enums.Direction
import com.engine.common.enums.Position
import com.engine.common.extensions.gdxArrayOf
import java.util.function.BiPredicate
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * A [GameRectangle] is a [Rectangle] that implements [PositionalGameShape2D]. It is used to represent a rectangle in a game.
 *
 * @see Rectangle
 * @see PositionalGameShape2D
 */
open class GameRectangle() : Rectangle(), PositionalGameShape2D {

    companion object {
        private var OVERLAP_EXTENSION: ((GameRectangle, IGameShape2D) -> Boolean)? = null

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameRectangle.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or
         * [GameLine]. This function should return true if the given [IGameShape2D] overlaps this
         * [GameRectangle] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameRectangle.overlaps] is called with a
         *   [IGameShape2D] that is not a [GameRectangle] or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: (GameRectangle, IGameShape2D) -> Boolean) {
            OVERLAP_EXTENSION = overlapExtension
        }

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameRectangle.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or
         * [GameLine]. This function should return true if the given [IGameShape2D] overlaps this
         * [GameRectangle] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameRectangle.overlaps] is called with a
         *  [IGameShape2D] that is not a [GameRectangle] or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: BiPredicate<GameRectangle, IGameShape2D>) {
            setOverlapExtension { rect, shape -> overlapExtension.test(rect, shape) }
        }

        /**
         * Calculates the bounds of a rectangle from the given lines.
         *
         * @param lines The lines to calculate the bounds from.
         * @return The bounds of the rectangle.
         */
        fun calculateBoundsFromLines(lines: Iterable<Pair<Vector2, Vector2>>): GameRectangle {
            var minX = Float.MAX_VALUE
            var minY = Float.MAX_VALUE
            var maxX = Float.NEGATIVE_INFINITY
            var maxY = Float.NEGATIVE_INFINITY

            lines.forEach { (point1, point2) ->
                minX = min(minX, min(point1.x, point2.x))
                minY = min(minY, min(point1.y, point2.y))
                maxX = max(maxX, max(point1.x, point2.x))
                maxY = max(maxY, max(point1.y, point2.y))
            }

            return GameRectangle(minX, minY, abs(maxX - minX), abs(maxY - minY))
        }
    }

    override var originX = 0f
    override var originY = 0f

    override var color: Color = RED
    override var shapeType = Line

    /** Thickness of the rectangle lines when drawn. This is used only if [shapeType] is [Line]. */
    var thickness: Float = 1f

    /**
     * Creates a new [GameRectangle] with the given first, second, width, and height.
     *
     * @param x The first coordinate of the bottom left corner of this rectangle.
     * @param y The second coordinate of the bottom left corner of this rectangle.
     * @param width The width of this rectangle.
     * @param height The height of this rectangle.
     * @see Rectangle
     */
    constructor(x: Number, y: Number, width: Number, height: Number) : this() {
        set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    }

    /**
     * Creates a new [GameRectangle] with the given [Rectangle].
     *
     * @param rect The [Rectangle] to create this [GameRectangle] from.
     * @see Rectangle
     */
    constructor(rect: Rectangle) : this() {
        set(rect)
    }

    /**
     * Sets the origin of this [GameRectangle] to the given [originX] and [originY].
     *
     * @param originX The x-coordinate of the origin.
     * @param originY The y-coordinate of the origin.
     * @return This [GameRectangle].
     */
    fun setOrigin(originX: Float, originY: Float): GameRectangle {
        this.originX = originX
        this.originY = originY
        return this
    }

    /**
     * Sets the x-coordinate of the origin of this [GameRectangle] to the given [originX].
     *
     * @param originX The x-coordinate of the origin.
     * @return This [GameRectangle].
     */
    fun setOriginX(originX: Float): GameRectangle {
        this.originX = originX
        return this
    }

    /**
     * Sets the y-coordinate of the origin of this [GameRectangle] to the given [originY].
     *
     * @param originY The y-coordinate of the origin.
     * @return This [GameRectangle].
     */
    fun setOriginY(originY: Float): GameRectangle {
        this.originY = originY
        return this
    }

    /**
     * Returns an array of [GameLine]s that make up this [GameRectangle].
     *
     * @return An array of [GameLine]s that make up this [GameRectangle].
     */
    fun getAsLines() =
        gdxArrayOf(
            GameLine(getTopLeftPoint(), getTopRightPoint()),
            GameLine(getBottomLeftPoint(), getBottomRightPoint()),
            GameLine(getBottomLeftPoint(), getTopLeftPoint()),
            GameLine(getBottomRightPoint(), getTopRightPoint())
        )

    /**
     * Returns an array of vertices that make up this [GameRectangle].
     *
     * @return An array of vertices that make up this [GameRectangle].
     */
    fun getVertices(): FloatArray {
        val vertices = FloatArray(8)
        vertices.addAll(x, y, x + width, y, x + width, y + height, x, y + height)
        return vertices
    }

    /**
     * Creates a new [GameRectangle] based on this instance and sets its rotation based on the given
     * [direction]. [Direction.UP] is 0f, [Direction.LEFT] is 90f, [Direction.DOWN] is 180f, and
     * [Direction.RIGHT] is 270f.
     *
     * @param direction The direction to set the rotation to.
     * @return A new [GameRectangle] based on this instance with the given rotation.
     */
    override fun getCardinallyRotatedShape(
        direction: Direction,
        useNewShape: Boolean,
    ): GameRectangle {
        val rotatedLines =
            getAsLines().map { line ->
                line.originX = originX
                line.originY = originY
                line.rotation =
                    when (direction) {
                        Direction.UP -> 0f
                        Direction.LEFT -> 90f
                        Direction.DOWN -> 180f
                        Direction.RIGHT -> 270f
                    }
                line.getWorldPoints()
            }

        val rotatedRectangle = calculateBoundsFromLines(rotatedLines)
        return if (useNewShape) rotatedRectangle else set(rotatedRectangle)
    }

    override fun setSize(sizeXY: Float) = super.setSize(sizeXY) as GameRectangle

    override fun setSize(width: Float, height: Float) = super.setSize(width, height) as GameRectangle

    fun setSize(size: Vector2) = setSize(size.x, size.y)

    fun getSize() = getSize(Vector2())

    final override fun set(x: Float, y: Float, width: Float, height: Float) =
        super.set(x, y, width, height) as GameRectangle

    final override fun set(rect: Rectangle) = super.set(rect) as GameRectangle

    override fun setX(x: Float) = super.setX(x) as GameRectangle

    override fun setY(y: Float) = super.setY(y) as GameRectangle

    override fun setWidth(width: Float) = super.setWidth(width) as GameRectangle

    override fun setHeight(height: Float) = super.setHeight(height) as GameRectangle

    override fun getPosition(): Vector2 = super<Rectangle>.getPosition(Vector2())

    override fun setPosition(position: Vector2) =
        super<Rectangle>.setPosition(position) as GameRectangle

    override fun setPosition(x: Float, y: Float) = super<Rectangle>.setPosition(x, y) as GameRectangle

    override fun merge(rect: Rectangle) = super.merge(rect) as GameRectangle

    override fun merge(x: Float, y: Float) = super.merge(x, y) as GameRectangle

    override fun merge(vec: Vector2) = super.merge(vec) as GameRectangle

    override fun merge(vecs: Array<out Vector2>) = super.merge(vecs) as GameRectangle

    override fun fitOutside(rect: Rectangle) = super.fitOutside(rect) as GameRectangle

    override fun fitInside(rect: Rectangle) = super.fitInside(rect) as GameRectangle

    override fun fromString(v: String) = super.fromString(v) as GameRectangle

    /**
     * Sets the x-coordinate of the right edge of the [GameRectangle] to the specified [maxX].
     *
     * @param maxX The new x-coordinate of the right edge.
     * @return The modified [GameRectangle].
     */
    fun setMaxX(maxX: Float) = setX(maxX - width)

    /**
     * Sets the y-coordinate of the bottom edge of the [GameRectangle] to the specified [maxY].
     *
     * @param maxY The new y-coordinate of the bottom edge.
     * @return The modified [GameRectangle].
     */
    fun setMaxY(maxY: Float) = setY(maxY - height)

    override fun getMaxX() = x + width

    override fun getMaxY() = y + height

    /**
     * Returns true if the given [IGameShape2D] overlaps this [GameRectangle] and false otherwise.
     * This method will call the [OVERLAP_EXTENSION] function if the given [IGameShape2D] is not a
     * [GameRectangle], [GameCircle], or [GameLine]. If the [OVERLAP_EXTENSION] function is not set,
     * this method will return false.
     *
     * @param other The [IGameShape2D] to check if it overlaps this [GameRectangle].
     * @return Whether the given [IGameShape2D] overlaps this [GameRectangle].
     */
    override fun overlaps(other: IGameShape2D) =
        when (other) {
            is GameRectangle -> Intersector.overlaps(this, other)
            is GameCircle -> Intersector.overlaps(other.libgdxCircle, this)
            is GameLine -> {
                val (worldPoint1, worldPoint2) = other.getWorldPoints()
                Intersector.intersectSegmentRectangle(worldPoint1, worldPoint2, this)
            }

            is GamePolygon -> Intersector.intersectPolygons(toPolygon().transformedVertices, other.transformedVertices)
            else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
        }

    override fun getBoundingRectangle() = GameRectangle(this)

    override fun getCenter(): Vector2 = super.getCenter(Vector2())

    override fun setCenter(centerX: Float, centerY: Float): GameRectangle {
        setCenterX(centerX)
        setCenterY(centerY)
        return this
    }

    override fun setCenter(center: Vector2): GameRectangle = setCenter(center.x, center.y)

    /**
     * Sets the x-coordinate of the center of the [GameRectangle] to the specified [centerX].
     *
     * @param centerX The new x-coordinate of the center.
     * @return The modified [GameRectangle].
     */
    fun setCenterX(centerX: Float): GameRectangle {
        super.setCenter(centerX, getCenter().y)
        return this
    }

    /**
     * Sets the y-coordinate of the center of the [GameRectangle] to the specified [centerY].
     *
     * @param centerY The new y-coordinate of the center.
     * @return The modified [GameRectangle].
     */
    fun setCenterY(centerY: Float): GameRectangle {
        super.setCenter(getCenter().x, centerY)
        return this
    }

    override fun translation(translateX: Float, translateY: Float): GameRectangle {
        x += translateX
        y += translateY
        return this
    }

    override fun draw(drawer: ShapeRenderer) {
        drawer.set(shapeType)
        drawer.color = color
        if (shapeType == Filled) drawer.rect(x, y, width, height)
        else if (shapeType == Line) {
            getAsLines().forEach {
                val worldPoints = it.getWorldPoints()
                drawer.rectLine(worldPoints.first, worldPoints.second, thickness)
            }
        }
    }

    override fun positionOnPoint(point: Vector2, position: Position): GameRectangle {
        when (position) {
            Position.TOP_LEFT -> setTopLeftToPoint(point)
            Position.TOP_CENTER -> setTopCenterToPoint(point)
            Position.TOP_RIGHT -> setTopRightToPoint(point)
            Position.CENTER_LEFT -> setCenterLeftToPoint(point)
            Position.CENTER -> setCenterToPoint(point)
            Position.CENTER_RIGHT -> setCenterRightToPoint(point)
            Position.BOTTOM_LEFT -> setBottomLeftToPoint(point)
            Position.BOTTOM_CENTER -> setBottomCenterToPoint(point)
            Position.BOTTOM_RIGHT -> setBottomRightToPoint(point)
        }
        return this
    }

    override fun getPositionPoint(position: Position) =
        when (position) {
            Position.TOP_LEFT -> getTopLeftPoint()
            Position.TOP_CENTER -> getTopCenterPoint()
            Position.TOP_RIGHT -> getTopRightPoint()
            Position.CENTER_LEFT -> getCenterLeftPoint()
            Position.CENTER -> getCenterPoint()
            Position.CENTER_RIGHT -> getCenterRightPoint()
            Position.BOTTOM_LEFT -> getBottomLeftPoint()
            Position.BOTTOM_CENTER -> getBottomCenterPoint()
            Position.BOTTOM_RIGHT -> getBottomRightPoint()
        }

    override fun copy(): GameRectangle = GameRectangle(this)

    /**
     * Sets the top left corner of this [GameRectangle] to the given point. Returns this
     * [GameRectangle].
     *
     * @param topLeftPoint The point to set the top left corner of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setTopLeftToPoint(topLeftPoint: Vector2): GameRectangle =
        setPosition(topLeftPoint.x, topLeftPoint.y - height)

    /**
     * Returns the top left corner of this [GameRectangle].
     *
     * @return The top left corner of this [GameRectangle].
     */
    fun getTopLeftPoint() = Vector2(x, y + height)

    /**
     * Sets the top center of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param topCenterPoint The point to set the top center of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setTopCenterToPoint(topCenterPoint: Vector2): GameRectangle =
        setPosition(topCenterPoint.x - (width / 2f), topCenterPoint.y - height)

    /**
     * Returns the top center of this [GameRectangle].
     *
     * @return The top center of this [GameRectangle].
     */
    fun getTopCenterPoint() = Vector2(x + (width / 2f), y + height)

    /**
     * Sets the top right corner of this [GameRectangle] to the given point. Returns this
     * [GameRectangle].
     *
     * @param topRightPoint The point to set the top right corner of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setTopRightToPoint(topRightPoint: Vector2): GameRectangle =
        setPosition(topRightPoint.x - width, topRightPoint.y - height)

    /**
     * Returns the top right corner of this [GameRectangle].
     *
     * @return The top right corner of this [GameRectangle].
     */
    fun getTopRightPoint() = Vector2(x + width, y + height)

    /**
     * Sets the center left of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param centerLeftPoint The point to set the center left of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setCenterLeftToPoint(centerLeftPoint: Vector2): GameRectangle =
        this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (this.height / 2f))

    /**
     * Returns the center left of this [GameRectangle].
     *
     * @return The center left of this [GameRectangle].
     */
    fun getCenterLeftPoint(): Vector2 = Vector2(this.x, this.y + (this.height / 2f))

    /**
     * Sets the center of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param centerPoint The point to set the center of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setCenterToPoint(centerPoint: Vector2): GameRectangle = this.setCenter(centerPoint)

    /**
     * Returns the center of this [GameRectangle].
     *
     * @return The center of this [GameRectangle].
     * @see Rectangle.getCenter
     */
    fun getCenterPoint(): Vector2 = this.getCenter(Vector2())

    /**
     * Sets the center right of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param centerRightPoint The point to set the center right of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setCenterRightToPoint(centerRightPoint: Vector2): GameRectangle =
        this.setPosition(centerRightPoint.x - this.width, centerRightPoint.y - (this.height / 2f))

    /**
     * Returns the center right of this [GameRectangle].
     *
     * @return The center right of this [GameRectangle].
     */
    fun getCenterRightPoint(): Vector2 = Vector2(this.x + this.width, this.y + (this.height / 2f))

    /**
     * Sets the bottom left of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param bottomLeftPoint The point to set the bottom left of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setBottomLeftToPoint(bottomLeftPoint: Vector2): GameRectangle =
        this.setPosition(bottomLeftPoint)

    /**
     * Returns the bottom left of this [GameRectangle].
     *
     * @return The bottom left of this [GameRectangle].
     */
    fun getBottomLeftPoint(): Vector2 = Vector2(this.x, this.y)

    /**
     * Sets the bottom center of this [GameRectangle] to the given point. Returns this
     * [GameRectangle].
     *
     * @param bottomCenterPoint The point to set the bottom center of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setBottomCenterToPoint(bottomCenterPoint: Vector2): GameRectangle =
        this.setPosition(bottomCenterPoint.x - this.width / 2f, bottomCenterPoint.y)

    /**
     * Returns the bottom center of this [GameRectangle].
     *
     * @return The bottom center of this [GameRectangle].
     */
    fun getBottomCenterPoint(): Vector2 = Vector2(this.x + this.width / 2f, this.y)

    /**
     * Sets the bottom right of this [GameRectangle] to the given point. Returns this [GameRectangle].
     *
     * @param bottomRightPoint The point to set the bottom right of this [GameRectangle] to.
     * @return This [GameRectangle].
     */
    fun setBottomRightToPoint(bottomRightPoint: Vector2): GameRectangle =
        this.setPosition(bottomRightPoint.x - this.width, bottomRightPoint.y)

    /**
     * Returns the bottom right of this [GameRectangle].
     *
     * @return The bottom right of this [GameRectangle].
     */
    fun getBottomRightPoint(): Vector2 = Vector2(this.x + this.width, this.y)

    /**
     * Converts this [GameRectangle] to a [GamePolygon].
     *
     * @return The [GamePolygon] created from this [GameRectangle].
     */
    fun toPolygon() = GamePolygon(getVertices())

    override fun equals(other: Any?) = other is Rectangle && super.equals(other)

    override fun hashCode() = super.hashCode()

    override fun toString() = super.toString()
}
