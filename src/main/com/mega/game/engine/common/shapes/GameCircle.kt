package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.pairTo
import java.util.function.BiPredicate

open class GameCircle(x: Float, y: Float, radius: Float) : IGameShape2D {

    companion object {

        private var OVERLAP_EXTENSION: ((GameCircle, IGameShape2D) -> Boolean)? = null

        fun setOverlapExtension(overlapExtension: (GameCircle, IGameShape2D) -> Boolean) {
            OVERLAP_EXTENSION = overlapExtension
        }

        fun setOverlapExtension(overlapExtension: BiPredicate<GameCircle, IGameShape2D>) {
            setOverlapExtension { circle, shape -> overlapExtension.test(circle, shape) }
        }
    }

    var x: Float
        get() = getX()
        set(value) {
            setX(value)
        }
    var y: Float
        get() = getY()
        set(value) {
            setY(value)
        }
    var radius: Float
        get() = getRadius()
        set(value) {
            setRadius(value)
        }

    internal val libgdxCircle: Circle = Circle(x, y, radius)
    private val tempRect = Rectangle()
    private val tempVec1 = Vector2()
    private val tempVec2 = Vector2()
    private val tempVec3 = Vector2()

    constructor() : this(0f, 0f, 0f)

    constructor(center: Vector2, radius: Float) : this(center.x, center.y, radius)

    constructor(circle: Circle) : this(circle.x, circle.y, circle.radius)

    override fun getProps(props: Properties): Properties {
        props.putAll(
            "x" pairTo libgdxCircle.x,
            "y" pairTo libgdxCircle.y,
            "radius" pairTo libgdxCircle.radius,
        )
        return props
    }

    override fun setWithProps(props: Properties): IGameShape2D {
        libgdxCircle.x = props.getOrDefault("x", libgdxCircle.x, Float::class)
        libgdxCircle.y = props.getOrDefault("y", libgdxCircle.y, Float::class)
        libgdxCircle.radius = props.getOrDefault("radius", libgdxCircle.radius, Float::class)
        return this
    }

    fun getRadius() = libgdxCircle.radius

    fun setRadius(radius: Float): GameCircle {
        libgdxCircle.radius = radius
        return this
    }

    fun getArea() = libgdxCircle.area()

    fun getCircumference() = libgdxCircle.circumference()

    override fun copy() = GameCircle(libgdxCircle.x, libgdxCircle.y, libgdxCircle.radius)

    override fun overlaps(other: IGameShape2D) =
        when (other) {
            is GameCircle -> libgdxCircle.overlaps(other.libgdxCircle)
            is GameRectangle -> Intersector.overlaps(libgdxCircle, other.get(tempRect))
            is GameLine -> {
                other.calculateWorldPoints(tempVec1, tempVec2)
                Intersector.intersectSegmentCircle(
                    tempVec1, tempVec2, getCenter(tempVec3), getRadius() * getRadius()
                )
            }

            is GamePolygon -> ShapeUtils.overlaps(this.libgdxCircle, other.libgdxPolygon)
            else -> OVERLAP_EXTENSION?.invoke(this, other) == true
        }

    override fun getBoundingRectangle(out: GameRectangle) = out.set(libgdxCircle.getBoundingRectangle())

    override fun setX(x: Float): IGameShape2D {
        libgdxCircle.x = x - getRadius()
        return this
    }

    override fun setY(y: Float): IGameShape2D {
        libgdxCircle.y = y - getRadius()
        return this
    }

    override fun getX() = libgdxCircle.x - getRadius()

    override fun getY() = libgdxCircle.y - getRadius()

    fun setMaxX(maxX: Float): IGameShape2D {
        libgdxCircle.x = maxX - getRadius()
        return this
    }

    fun setMaxY(maxY: Float): IGameShape2D {
        libgdxCircle.y = maxY - getRadius()
        return this
    }

    override fun getMaxX() = libgdxCircle.x + getRadius()

    override fun getMaxY() = libgdxCircle.y + getRadius()

    override fun getCenter(out: Vector2): Vector2 = out.set(getX(), getY())

    override fun setCenter(center: Vector2) = setCenter(center.x, center.y)

    override fun setCenter(centerX: Float, centerY: Float): IGameShape2D {
        libgdxCircle.setPosition(centerX, centerY)
        return this
    }

    fun setCenterX(centerX: Float): IGameShape2D {
        libgdxCircle.x = centerX
        return this
    }

    fun setCenterY(centerY: Float): IGameShape2D {
        libgdxCircle.y = centerY
        return this
    }

    override fun translate(translateX: Float, translateY: Float): IGameShape2D {
        libgdxCircle.x += translateX
        libgdxCircle.y += translateY
        return this
    }

    override fun getWidth() = getRadius()

    override fun getHeight() = getRadius()

    override fun contains(point: Vector2) = libgdxCircle.contains(point)

    override fun contains(x: Float, y: Float) = libgdxCircle.contains(x, y)

    override fun equals(other: Any?) = other is GameCircle && libgdxCircle == other.libgdxCircle

    override fun hashCode() = libgdxCircle.hashCode()
}
