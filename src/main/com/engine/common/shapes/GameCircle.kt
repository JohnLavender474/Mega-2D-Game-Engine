package com.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.engine.common.enums.Direction
import java.util.function.BiPredicate

/**
 * A circle that can be used in a game. This circle is a [Circle]. For convenience reasons, this
 * also extends [IGameShape2DSupplier].
 *
 * @param x The x-coordinate of the circle's center.
 * @param y The y-coordinate of the circle's center.
 * @param radius The radius of the circle.
 */
class GameCircle(x: Float, y: Float, radius: Float) : IGameShape2D {

    companion object {
        private var OVERLAP_EXTENSION: ((GameCircle, IGameShape2D) -> Boolean)? = null

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameCircle.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle],
         * [GameCircle], or [GameLine]. This function should return true if the given [IGameShape2D]
         * overlaps this [GameCircle] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameCircle.overlaps] is called with a
         *   [IGameShape2D] that is not a [GameRectangle], [GameCircle], or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: (GameCircle, IGameShape2D) -> Boolean) {
            OVERLAP_EXTENSION = overlapExtension
        }

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameCircle.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle],
         * [GameCircle], or [GameLine]. This function should return true if the given [IGameShape2D]
         * overlaps this [GameCircle] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameCircle.overlaps] is called with a
         *  [IGameShape2D] that is not a [GameRectangle], [GameCircle], or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: BiPredicate<GameCircle, IGameShape2D>) {
            setOverlapExtension { circle, shape -> overlapExtension.test(circle, shape) }
        }
    }

    val libgdxCircle: Circle

    override var originX = 0f
    override var originY = 0f

    override var color: Color = Color.RED
    override var shapeType: ShapeType = Line

    init {
        libgdxCircle = Circle(x, y, radius)
    }

    /** Creates a circle with the center at [0,0] and a radius of 0. */
    constructor() : this(0f, 0f, 0f)

    /**
     * Creates a circle with the given center and radius.
     *
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     */
    constructor(center: Vector2, radius: Float) : this(center.x, center.y, radius)

    /**
     * Creates a game circle with the given [Circle].
     *
     * @param circle The circle to copy.
     * @return A new [GameCircle] with the given [Circle].
     */
    constructor(circle: Circle) : this(circle.x, circle.y, circle.radius)

    /**
     * Returns a new [GameCircle] with the given cardinal rotation.
     *
     * @param direction The cardinal rotation to rotate this shape by.
     * @return A new [GameCircle] with the given cardinal rotation.
     */
    override fun getCardinallyRotatedShape(direction: Direction, useNewShape: Boolean): IGameShape2D {
        val rotatedBoundingRectangle =
            getBoundingRectangle()
                .setOrigin(originX, originY)
                .getCardinallyRotatedShape(direction, false)
        val rotatedCircle = if (useNewShape) copy() else this
        return rotatedCircle.setCenter(rotatedBoundingRectangle.getCenter())
    }

    /**
     * Gets the radius of the circle.
     *
     * @return The radius of the circle.
     */
    fun getRadius() = libgdxCircle.radius

    /**
     * Sets the radius of the circle.
     *
     * @param radius The radius to set.
     * @return This circle.
     */
    fun setRadius(radius: Float): GameCircle {
        libgdxCircle.radius = radius
        return this
    }

    /**
     * Gets the area of the circle.
     *
     * @return The area of the circle.
     */
    fun getArea() = libgdxCircle.area()

    /**
     * Gets the circumference of the circle.
     *
     * @return The circumference of the circle.
     */
    fun getCircumference() = libgdxCircle.circumference()

    /**
     * Returns a copy of this circle.
     *
     * @return A copy of this circle.
     */
    override fun copy() = GameCircle(libgdxCircle.x, libgdxCircle.y, libgdxCircle.radius)

    /**
     * Checks if this circle overlaps with another [IGameShape2D]. If the other shape is a
     * [GameRectangle], [GameCircle], or [GameLine], then the appropriate overlap method is called.
     * Otherwise, the overlap extension function is called.
     *
     * @param other The shape to check for overlap.
     * @return `true` if this circle overlaps with the given shape, `false` otherwise.
     */
    override fun overlaps(other: IGameShape2D) =
        when (other) {
            is GameCircle -> libgdxCircle.overlaps(other.libgdxCircle)
            is GameRectangle -> Intersector.overlaps(libgdxCircle, other)
            is GameLine -> {
                val (worldPoint1, worldPoint2) = other.getWorldPoints()
                Intersector.intersectSegmentCircle(
                    worldPoint1, worldPoint2, getCenter(), getRadius() * getRadius()
                )
            }

            is GamePolygon -> ShapeUtils.overlaps(this.libgdxCircle, other.libgdxPolygon)
            else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
        }

    /**
     * Gets the bounding rectangle of this circle.
     *
     * @return The bounding rectangle of this circle.
     */
    override fun getBoundingRectangle() = libgdxCircle.getBoundingRectangle()

    /**
     * Sets the least x-coordinate.
     *
     * @param x The least x-coordinate.
     * @return This shape.
     */
    override fun setX(x: Float): IGameShape2D {
        libgdxCircle.x = x - getRadius()
        return this
    }

    /**
     * Sets the least y-coordinate.
     *
     * @param y The least y-coordinate.
     * @return This shape.
     */
    override fun setY(y: Float): IGameShape2D {
        libgdxCircle.y = y - getRadius()
        return this
    }

    /**
     * Gets the least x-coordinate.
     *
     * @return The least x-coordinatek.
     */
    override fun getX() = libgdxCircle.x - getRadius()

    /**
     * Gets the least y-coordinate.
     *
     * @return The least y-coordinate.
     */
    override fun getY() = libgdxCircle.y - getRadius()

    /**
     * Sets the x-coordinate of the right-most coordinate.
     *
     * @param maxX The x-coordinate of the top-right corner coordinate to set.
     * @return This shape.
     */
    fun setMaxX(maxX: Float): IGameShape2D {
        libgdxCircle.x = maxX - getRadius()
        return this
    }

    /**
     * Sets the y-coordinate of the top-most coordinate.
     *
     * @param maxY The y-coordinate of the top-right corner coordinate to set.
     * @return This shape.
     */
    fun setMaxY(maxY: Float): IGameShape2D {
        libgdxCircle.y = maxY - getRadius()
        return this
    }

    /**
     * Gets the x-coordinate of the top-right corner coordinate of the bounding rectangle.
     *
     * @return The x-coordinate of the top-right corner coordinate.
     */
    override fun getMaxX() = libgdxCircle.x + getRadius()

    /**
     * Gets the y-coordinate of the top-right corner coordinate of the bounding rectangle.
     *
     * @return The y-coordinate of the top-right corner coordinate.
     */
    override fun getMaxY() = libgdxCircle.y + getRadius()

    /**
     * Gets the center of the circle.
     *
     * @return The center of the circle.
     */
    override fun getCenter() = Vector2(getX(), getY())

    /**
     * Sets the center of the circle.
     *
     * @param center The center to set.
     * @return This shape.
     */
    override fun setCenter(center: Vector2) = setCenter(center.x, center.y)

    /**
     * Sets the center of the circle.
     *
     * @param centerX The x-coordinate of the center to set.
     * @param centerY The y-coordinate of the center to set.
     * @return This shape.
     */
    override fun setCenter(centerX: Float, centerY: Float): IGameShape2D {
        libgdxCircle.setPosition(centerX, centerY)
        return this
    }

    /**
     * Sets the center x-coordinate of the center of the circle.
     *
     * @param centerX The x-coordinate of the center to set.
     * @return This shape.
     */
    fun setCenterX(centerX: Float): IGameShape2D {
        libgdxCircle.x = centerX
        return this
    }

    /**
     * Sets the center y-coordinate of the center of the circle.
     *
     * @param centerY The y-coordinate of the center to set.
     * @return This shape.
     */
    fun setCenterY(centerY: Float): IGameShape2D {
        libgdxCircle.y = centerY
        return this
    }

    /**
     * Translates this shape by the given translation.
     *
     * @param translateX The translation in the x-direction.
     * @param translateY The translation in the y-direction.
     * @return This shape.
     */
    override fun translation(translateX: Float, translateY: Float): IGameShape2D {
        libgdxCircle.x += translateX
        libgdxCircle.y += translateY
        return this
    }

    /**
     * Checks if this circle contains the given point.
     *
     * @param point The point to check.
     * @return `true` if this circle contains the given point, `false` otherwise.
     */
    override fun contains(point: Vector2) = libgdxCircle.contains(point)

    /**
     * Checks if this circle contains the given point.
     *
     * @param x The x-coordinate of the point to check.
     * @param y The y-coordinate of the point to check.
     * @return `true` if this circle contains the given point, `false` otherwise.
     */
    override fun contains(x: Float, y: Float) = libgdxCircle.contains(x, y)

    /**
     * Draws this shape using the given [ShapeRenderer].
     *
     * @param drawer The [ShapeRenderer] to use to draw this shape.
     */
    override fun draw(drawer: ShapeRenderer) {
        drawer.set(shapeType)
        drawer.color = color
        drawer.circle(libgdxCircle.x, libgdxCircle.y, libgdxCircle.radius)
    }

    /**
     * Checks if this circle is equal to the given object. This checks if the composed [libgdxCircle]s
     * are equal.
     *
     * @param other The object to check for equality.
     * @return `true` if this circle is equal to the given object, `false` otherwise.
     */
    override fun equals(other: Any?) = other is GameCircle && libgdxCircle == other.libgdxCircle

    /**
     * Gets the hash code of this circle. Returns the hashcode of [libgdxCircle].
     *
     * @return The hash code of this circle.
     */
    override fun hashCode() = libgdxCircle.hashCode()
}
