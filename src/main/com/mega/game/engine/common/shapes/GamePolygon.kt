package com.mega.game.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.FloatArray
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.props
import com.mega.game.engine.common.objects.pairTo
import java.util.function.BiPredicate

/**
 * A polygon is defined as vertices that may or may not create a connected shape.
 */
open class GamePolygon() : ICardinallyRotatableShape2D {

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
     * Returns this circle's properties in a [Properties] object. The following key-value entries are returned:
     * - "local_vertices": FloatArray
     * - "x": Float
     * - "y": Float
     * - "scale_x": Float
     * - "scale_y": Float
     * - "origin_x": Float
     * - "origin_y": Float
     * - "rotation": Float
     * - "color": Color
     * - "shape_type": ShapeType
     *
     * @see Color
     * @see ShapeRenderer.ShapeType
     */
    override fun getProps(props: Properties?): Properties {
        val returnProps = props ?: props()
        returnProps.putAll(
            "local_vertices" pairTo FloatArray(localVertices),
            "x" pairTo getX(),
            "y" pairTo getY(),
            "scale_x" pairTo scaleX,
            "scale_y" pairTo scaleY,
            "origin_x" pairTo originX,
            "origin_y" pairTo originY,
            "rotation" pairTo rotation,
            "color" pairTo color,
            "shape_type" pairTo shapeType
        )
        return returnProps
    }

    /**
     * Sets this circle's properties with the specified [Properties]. See [getProps] for the expected key-value entries.
     *
     * @param props the properties
     * @return this shape for chaining
     */
    override fun setWithProps(props: Properties): IGameShape2D {
        if (props.containsKey("local_vertices")) localVertices = props.get("local_vertices", FloatArray::class)!!
        if (props.containsKey("x")) setX(props.get("x", Float::class)!!)
        if (props.containsKey("y")) setY(props.get("y", Float::class)!!)
        if (props.containsKey("scale_x")) scaleX = props.get("scale_x", Float::class)!!
        if (props.containsKey("scale_y")) scaleY = props.get("scale_y", Float::class)!!
        if (props.containsKey("origin_x")) originX = props.get("origin_x", Float::class)!!
        if (props.containsKey("origin_y")) originY = props.get("origin_y", Float::class)!!
        if (props.containsKey("rotation")) rotation = props.get("rotation", Float::class)!!
        if (props.containsKey("color")) color = props.get("color", Color::class)!!
        if (props.containsKey("shape_type")) shapeType = props.get("shape_type", ShapeType::class)!!
        return this
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

    override fun getBoundingRectangle(bounds: GameRectangle?): GameRectangle {
        val polygonBounds = bounds ?: GameRectangle()
        return polygonBounds.set(libgdxPolygon.boundingRectangle)
    }

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

    /**
     * Returns the width of this polygon's bounding rectangle.
     *
     * @return the width of this polygon's bounding rectangle.
     */
    override fun getWidth() = getBoundingRectangle().width

    /**
     * Returns the height of this polygon's bounding rectangle.
     *
     * @return the height of this polygon's bounding rectangle.
     */
    override fun getHeight() = getBoundingRectangle().height

    override fun contains(p0: Vector2) = libgdxPolygon.contains(p0)

    override fun contains(p0: Float, p1: Float) = libgdxPolygon.contains(p0, p1)

    override fun draw(drawer: ShapeRenderer) {
        drawer.color = color
        drawer.set(shapeType)
        drawer.polygon(libgdxPolygon.transformedVertices)
    }

    override fun copy() = GamePolygon(this)

    /**
     * Returns a new polygon created from this line rotated by the given amount based on the [direction]. The [direction]
     * is used to determine the direction to rotate this line. [Direction.UP] is 90 degrees, [Direction.RIGHT] is 180
     * degrees, [Direction.DOWN] is 270 degrees, and [Direction.LEFT] is 360 degrees.
     *
     * The [returnShape] type must either be null or else a [GamePolygon], or else an exception will be thrown.
     *
     * @param direction The direction to rotate this polygon.
     * @param returnShape The shape to rotate and return.
     * @return The rotated polygon.
     */
    override fun getCardinallyRotatedShape(direction: Direction, returnShape: IGameShape2D?): GamePolygon {
        val rotatedPoly = when (returnShape) {
            null -> this
            is GamePolygon -> returnShape
            else -> throw IllegalStateException("Return shape is not a GamePolygon: $returnShape")
        }
        when (direction) {
            Direction.UP -> rotatedPoly.libgdxPolygon.rotation = 0f
            Direction.DOWN -> rotatedPoly.libgdxPolygon.rotation = 180f
            Direction.LEFT -> rotatedPoly.libgdxPolygon.rotation = 90f
            Direction.RIGHT -> rotatedPoly.libgdxPolygon.rotation = 270f
        }
        return rotatedPoly
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