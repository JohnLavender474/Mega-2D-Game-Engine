package com.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.FloatArray
import com.engine.common.enums.Direction
import java.util.function.BiPredicate

/**
 * A polygon is defined as vertices that may or may not create a connected shape.
 */
class GamePolygon() : IGameShape2D {

    companion object {
        private var OVERLAP_EXTENSION: ((GamePolygon, IGameShape2D) -> Boolean)? = null

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameRectangle.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or
         * [GameLine]. This function should return true if the given [IGameShape2D] overlaps this
         * [GameRectangle] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameRectangle.overlaps] is called with a
         *   [IGameShape2D] that is not a [GameRectangle] or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: (GamePolygon, IGameShape2D) -> Boolean) {
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
        fun setOverlapExtension(overlapExtension: BiPredicate<GamePolygon, IGameShape2D>) {
            setOverlapExtension { polygon, shape -> overlapExtension.test(polygon, shape) }
        }
    }

    val libgdxPolygon = Polygon()

    /**
     * A copy of the transformed vertices of the polygon.
     */
    val transformedVertices: FloatArray
        get() = FloatArray(libgdxPolygon.transformedVertices)

    /**
     * The number of vertices in the polygon.
     */
    val vertexCount: Int
        get() = libgdxPolygon.vertexCount

    override var originX: Float
        get() = libgdxPolygon.originX
        set(value) = libgdxPolygon.setOrigin(value, originY)

    override var originY: Float
        get() = libgdxPolygon.originY
        set(value) = libgdxPolygon.setOrigin(originX, value)

    override var color: Color = Color.RED
    override var shapeType = ShapeRenderer.ShapeType.Line

    /**
     * The local vertices of the polygon. The getter of this field returns a copy of the local vertices.
     * The setter of this field converts the value to a Kotlin float array and sets the local vertices of
     * the internal LibGDX [Polygon].
     */
    var localVertices: FloatArray
        get() = FloatArray(libgdxPolygon.vertices)
        set(value) {
            libgdxPolygon.vertices = value.toArray()
        }

    /**
     * The x-scale factor of the polygon.
     */
    var scaleX: Float
        get() = libgdxPolygon.scaleX
        set(value) {
            libgdxPolygon.setScale(value, libgdxPolygon.scaleY)
        }

    /**
     * The y-scale factor of the polygon.
     */
    var scaleY: Float
        get() = libgdxPolygon.scaleY
        set(value) {
            libgdxPolygon.setScale(libgdxPolygon.scaleX, value)
        }

    /**
     * The rotation of the polygon.
     */
    var rotation: Float
        get() = libgdxPolygon.rotation
        set(value) {
            libgdxPolygon.rotation = value
        }

    /**
     * Constructs a new polygon with the given vertices. The vertices should be in the form of [x1, y1, x2, y2, ...].
     * The array passed into the constructor is copied to prevent external modification.
     *
     * @param vertices The vertices of the new polygon
     */
    constructor(vertices: FloatArray) : this() {
        libgdxPolygon.vertices = vertices.toArray()
    }

    /**
     * Constructs a new polygon using the fields of the given polygon. The local vertices are copied to prevent
     * external modification.
     *
     * @param polygon The polygon from which to copy
     */
    constructor(polygon: GamePolygon) : this() {
        localVertices = FloatArray(polygon.localVertices)
        setX(polygon.getX())
        setY(polygon.getY())
        scaleX = polygon.scaleX
        scaleY = polygon.scaleY
        originX = polygon.originX
        originY = polygon.originY
        rotation = polygon.rotation
        color = polygon.color
        shapeType = polygon.shapeType
    }

    /**
     * Constructs a new polygon using the fields of the given polygon. The local vertices are copied to prevent
     * external modification.
     *
     * @param polygon The polygon from which to copy
     */
    constructor(polygon: Polygon) : this() {
        localVertices = FloatArray(polygon.vertices)
        setX(polygon.x)
        setY(polygon.y)
        scaleX = polygon.scaleX
        scaleY = polygon.scaleY
        originX = polygon.originX
        originY = polygon.originY
        rotation = polygon.rotation
    }

    /**
     * Sets the local vertices of the polygon to be the same as the given vertices. The vertices are copied to prevent
     * external modification. The scale, origin, and rotation are also set to values as the given polygon.
     *
     * @param polygon The polygon from which to copy
     * @return This polygon for chaining
     */
    fun set(polygon: GamePolygon): GamePolygon {
        color = polygon.color
        shapeType = polygon.shapeType
        return set(polygon.libgdxPolygon)
    }

    /**
     * Sets the local vertices of the polygon to be the same as the given polygon. The local vertices are copied to
     * prevent external modification. The scale, origin, and rotation are also set to the same values as the given
     * polygon.
     *
     * @param polygon The polygon from which to copy
     * @return This polygon for chaining
     */
    fun set(polygon: Polygon): GamePolygon {
        localVertices = FloatArray(polygon.vertices)
        setX(polygon.x)
        setY(polygon.y)
        scaleX = polygon.scaleX
        scaleY = polygon.scaleY
        originX = polygon.originX
        originY = polygon.originY
        rotation = polygon.rotation
        return this
    }

    override fun overlaps(other: IGameShape2D): Boolean {
        return when (other) {
            is GamePolygon -> Intersector.overlapConvexPolygons(libgdxPolygon, other.libgdxPolygon)
            is GameRectangle -> Intersector.overlapConvexPolygons(libgdxPolygon, other.toPolygon().libgdxPolygon)
            is GameLine -> Intersector.overlapConvexPolygons(libgdxPolygon, Polygon(other.getTransformedVertcies()))
            is GameCircle -> ShapeUtils.overlaps(other.libgdxCircle, libgdxPolygon)
            else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
        }
    }

    override fun getBoundingRectangle() = libgdxPolygon.boundingRectangle.toGameRectangle()

    override fun setPosition(x: Float, y: Float): IGameShape2D {
        libgdxPolygon.setPosition(x, y)
        return this
    }

    override fun setX(x: Float): IGameShape2D {
        libgdxPolygon.setPosition(x, libgdxPolygon.y)
        return this
    }

    override fun setY(y: Float): IGameShape2D {
        libgdxPolygon.setPosition(libgdxPolygon.x, y)
        return this
    }

    override fun getX() = libgdxPolygon.x

    override fun getY() = libgdxPolygon.y

    override fun getCenter() = libgdxPolygon.boundingRectangle.getCenter()

    override fun setCenter(center: Vector2) = setCenter(center.x, center.y)

    override fun setCenter(centerX: Float, centerY: Float): IGameShape2D {
        val bounds = libgdxPolygon.boundingRectangle
        libgdxPolygon.setPosition(centerX - bounds.width / 2, centerY - bounds.height / 2)
        return this
    }

    override fun getMaxX(): Float {
        val bounds = libgdxPolygon.boundingRectangle
        return bounds.x + bounds.width
    }

    override fun getMaxY(): Float {
        val bounds = libgdxPolygon.boundingRectangle
        return bounds.y + bounds.height
    }

    override fun translation(translateX: Float, translateY: Float): IGameShape2D {
        libgdxPolygon.translate(translateX, translateY)
        return this
    }

    override fun contains(p0: Vector2) = libgdxPolygon.contains(p0)

    override fun contains(p0: Float, p1: Float) = libgdxPolygon.contains(p0, p1)

    override fun draw(drawer: ShapeRenderer) {
        drawer.color = color
        drawer.set(shapeType)
        drawer.polygon(libgdxPolygon.transformedVertices)
    }

    override fun copy() = GamePolygon(this)

    override fun getCardinallyRotatedShape(direction: Direction, useNewShape: Boolean): IGameShape2D {
        val polygon = if (useNewShape) copy() else this
        when (direction) {
            Direction.UP -> polygon.libgdxPolygon.rotation = 0f
            Direction.DOWN -> polygon.libgdxPolygon.rotation = 180f
            Direction.LEFT -> polygon.libgdxPolygon.rotation = 90f
            Direction.RIGHT -> polygon.libgdxPolygon.rotation = 270f
        }
        return polygon
    }

    /**
     * Set vertex position
     * @param vertexNum min=0, max=vertices.length/2-1
     * @throws IllegalArgumentException if vertex doesnt exist
     */
    fun setVertex(vertexNum: Int, x: Float, y: Float) = libgdxPolygon.setVertex(vertexNum, x, y)

    /**
     * Applies additional rotation to the polygon by the supplied degrees.
     *
     * @param rotation The degrees to apply
     */
    fun rotate(rotation: Float) = libgdxPolygon.rotate(rotation)

    /**
     * Applies additional scaling to the polygon by the supplied amount.
     *
     * @param scale The scaling to apply
     */
    fun scale(scale: Float) = libgdxPolygon.scale(scale)

    /**
     * Sets the polygon's world vertices to be recalculated when calling [transformedVertices].
     */
    fun setDirty() = libgdxPolygon.dirty()

    /**
     * Returns the area contained within the polygon.
     *
     * @return The area of the polygon
     */
    fun area() = libgdxPolygon.area()

    /**
     * The transformed position of the vertex at the specified index.
     *
     * @param vertexIndex The vertex index
     * @param pos The vector that will hold the position
     */
    fun getVertex(vertexIndex: Int, pos: Vector2) = libgdxPolygon.getVertex(vertexIndex, pos)

    /**
     * Stores the centroid of the polygon into the supplied vector.
     *
     * @param centroid The vector to hold the centroid value
     */
    fun getCentroid(centroid: Vector2): Vector2 = libgdxPolygon.getCentroid(centroid)

    override fun toString() = "GamePolygon(vertices=$transformedVertices)"
}