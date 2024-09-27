package com.mega.game.engine.common.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.common.interfaces.IRotatable
import com.mega.game.engine.common.interfaces.IScalable
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.props
import com.mega.game.engine.common.objects.pairTo
import java.util.function.BiPredicate
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A line that can be used in a game. A line is defined by two pointsHandles.
 */
class GameLine : IGameShape2D, ICardinallyRotatableShape2D, IScalable, IRotatable {

    companion object {
        private var OVERLAP_EXTENSION: ((GameLine, IGameShape2D) -> Boolean)? = null

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameLine.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or
         * [GameLine]. This function should return true if the given [IGameShape2D] overlaps this
         * [GameLine] and false otherwise.
         *
         * @param overlapExtension The function to call when [GameLine.overlaps] is called with a
         *   [IGameShape2D] that is not a [GameRectangle] or [GameLine].
         */
        fun setOverlapExtension(overlapExtension: (GameLine, IGameShape2D) -> Boolean) {
            OVERLAP_EXTENSION = overlapExtension
        }

        /**
         * Sets the overlap extension function to the given function. This function will be called when
         * [GameLine.overlaps] is called with a [IGameShape2D] that is not a [GameRectangle] or [GameLine].
         *
         * @param overlapExtension The function to call when [GameLine.overlaps] is called with a
         */
        fun setOverlapExtension(overlapExtension: BiPredicate<GameLine, IGameShape2D>) {
            OVERLAP_EXTENSION = overlapExtension::test
        }
    }

    override var color: Color = Color.RED
    override var shapeType: ShapeType = Line

    /** The thickness of this line when drawn. */
    var thickness: Float = 1f

    private val position = Vector2()
    private val localPoint1 = Vector2()
    private val localPoint2 = Vector2()
    private val worldPoint1 = Vector2()
    private val worldPoint2 = Vector2()

    override var rotation = 0f
        set(value) {
            field = value
            dirty = true
        }

    override var originX = 0f
        set(value) {
            field = value
            dirty = true
        }

    override var originY = 0f
        set(value) {
            field = value
            dirty = true
        }

    override var scaleX = 1f
        set(value) {
            field = value
            dirty = true
            calculateScaledLength = true
        }

    override var scaleY = 1f
        set(value) {
            field = value
            dirty = true
            calculateScaledLength = true
        }

    private var dirty = true

    private var length = 0f
    private var calculateLength = true

    private var scaledLength = 0f
    private var calculateScaledLength = true

    /**
     * Sets the game line to the following local points.
     * @param x1 the first x coordinate
     * @param y1 the first y coordinate
     * @param x2 the second x coordinate
     * @param y2 the second y coordinate
     */
    constructor(x1: Float, y1: Float, x2: Float, y2: Float) {
        localPoint1.set(x1, y1)
        localPoint2.set(x2, y2)
    }

    /**
     * Creates a new line with the given line. Copies all the fields from the given [GameLine]
     * to this line.
     *
     * @param line The line to copy.
     * @return A line with the given fields copied.
     */
    constructor(line: GameLine) {
        localPoint1.set(line.localPoint1)
        localPoint2.set(line.localPoint2)
        scaleX = line.scaleX
        scaleY = line.scaleY
        rotation = line.rotation
        originX = line.originX
        originY = line.originY
        color = line.color
        shapeType = line.shapeType
        thickness = line.thickness
    }

    /**
     * Creates a line with the given points.
     *
     * @param point1 The first point of this line.
     * @param point2 The second point of this line.
     */
    constructor(point1: Vector2, point2: Vector2) : this(point1.x, point1.y, point2.x, point2.y)

    /** Creates a line with points at [0,0] and [0,0] */
    constructor() : this(0f, 0f, 0f, 0f)

    /**
     * Returns this line's properties in a [Properties] object. The following key-value entries are set:
     * - "local_point_1_x": Float
     * - "local_point_1_y": Float
     * - "local_point_2_x": Float
     * - "local_point_2_y": Float
     * - "scale_x": Float
     * - "scale_y": Float
     * - "rotation": Float
     * - "origin_x": Float
     * - "origin_y": Float
     * - "color": Color
     * - "shape_type": ShapeType
     * - "thickness": Float
     *
     * @return this shape's properties
     */
    override fun getProps(props: Properties?): Properties {
        val returnProps = props ?: props()
        returnProps.putAll(
            "local_point_1_x" pairTo localPoint1.x,
            "local_point_1_y" pairTo localPoint1.y,
            "local_point_2_x" pairTo localPoint2.x,
            "local_point_2_y" pairTo localPoint2.y,
            "scale_x" pairTo scaleX,
            "scale_y" pairTo scaleY,
            "rotation" pairTo rotation,
            "origin_x" pairTo originX,
            "origin_y" pairTo originY,
            "color" pairTo color,
            "shape_type" pairTo shapeType,
            "thickness" pairTo thickness
        )
        return returnProps
    }

    /**
     * Sets this circle's properties with the specified [Properties]. See [getProps] for a list of the expected
     * key-value pairs.
     *
     * @param props the properties
     * @return this shape for chaining
     */
    override fun setWithProps(props: Properties): IGameShape2D {
        localPoint1.x = props.getOrDefault("local_point_1_x", localPoint1.x, Float::class)
        localPoint1.y = props.getOrDefault("local_point_1_y", localPoint1.y, Float::class)
        localPoint2.x = props.getOrDefault("local_point_2_x", localPoint2.x, Float::class)
        localPoint2.y = props.getOrDefault("local_point_2_y", localPoint2.y, Float::class)
        scaleX = props.getOrDefault("scale_x", scaleX, Float::class)
        scaleY = props.getOrDefault("scale_y", scaleX, Float::class)
        rotation = props.getOrDefault("rotation", rotation, Float::class)
        originX = props.getOrDefault("origin_x", originX, Float::class)
        originY = props.getOrDefault("origin_y", originY, Float::class)
        color = props.getOrDefault("color", color, Color::class)
        shapeType = props.getOrDefault("shape_type", shapeType, ShapeType::class)
        thickness = props.getOrDefault("thickness", thickness, Float::class)
        return this
    }

    /**
     * Returns the vertices of this line as a float array. The vertices are the local points of this line.
     *
     * @return The vertices of this line.
     */
    fun getVertices(): FloatArray {
        return floatArrayOf(localPoint1.x, localPoint1.y, localPoint2.x, localPoint2.y)
    }

    /**
     * Returns the transformed vertices of this line as a float array. The vertices are the world points
     * of this line.
     *
     * @return The transformed vertices of this line.
     */
    fun getTransformedVertcies(): FloatArray {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        return floatArrayOf(_worldPoint1.x, _worldPoint1.y, _worldPoint2.x, _worldPoint2.y)
    }

    /**
     * Sets this line to the provided [GameLine]. This sets the [position], [originX], [originY],
     * [localPoint1], and [localPoint2].
     *
     * @param line The line to copy.
     * @return This line.
     */
    fun set(line: GameLine): GameLine {
        setPosition(line.position)
        setOrigin(line.originX, line.originY)
        scaleX = line.scaleX
        scaleY = line.scaleY
        rotation = line.rotation
        return set(line.localPoint1, line.localPoint2)
    }

    /**
     * Sets the points of this line to the given points.
     *
     * @param point1 The first point of this line.
     * @param point2 The second point of this line.
     * @return This line.
     */
    fun set(point1: Vector2, point2: Vector2) = set(point1.x, point1.y, point2.x, point2.y)

    /**
     * Sets the points of this line to the given points.
     *
     * @param x1 The x-coordinate of the first point of this line.
     * @param y1 The y-coordinate of the first point of this line.
     * @param x2 The x-coordinate of the second point of this line.
     * @param y2 The y-coordinate of the second point of this line.
     * @return This line.
     */
    fun set(x1: Float, y1: Float, x2: Float, y2: Float): GameLine {
        localPoint1.x = x1
        localPoint1.y = y1
        localPoint2.x = x2
        localPoint2.y = y2
        dirty = true
        calculateLength = true
        return this
    }

    /**
     * Sets the origin of rotation to the center of the line. If the line is moved, set to dirty, or any other change
     * is made, then this method should be called again to recalculate the origin to the center.
     */
    fun setOriginCenter() {
        originX = getCenter().x
        originY = getCenter().y
    }

    /** Sets this line to dirty which means that the world points need to be recalculated. */
    fun setToDirty() {
        dirty = true
    }

    /** Sets to recalculate the length of this line. */
    fun setToRecalculateLength() {
        calculateLength = true
    }

    /**
     * Returns a new line created from this line rotated by the given amount based on the [direction]. The [direction]
     * is used to determine the direction to rotate this line. [Direction.UP] is 90 degrees, [Direction.RIGHT] is 180
     * degrees, [Direction.DOWN] is 270 degrees, and [Direction.LEFT] is 360 degrees.
     *
     * The [returnShape] type must either be null or else a [GameLine], or else an exception will be thrown.
     *
     * @param direction The direction to rotate this line.
     * @param returnShape The shape to rotate and return.
     * @return The rotated line.
     */
    override fun getCardinallyRotatedShape(direction: Direction, returnShape: IGameShape2D?): GameLine {
        val rotatedLine = when (returnShape) {
            null -> this
            is GameLine -> returnShape
            else -> throw IllegalStateException("Return shape is not a GameLine: $returnShape")
        }
        rotatedLine.rotation = when (direction) {
            Direction.UP -> 0f
            Direction.RIGHT -> 90f
            Direction.DOWN -> 180f
            Direction.LEFT -> 270f
        }
        return rotatedLine
    }

    /**
     * Calculates and returns the length of this line.
     *
     * @return The length of this line.
     */
    fun getLength(): Float {
        if (calculateLength) {
            calculateLength = false
            val x = localPoint1.x - localPoint2.x
            val y = localPoint1.y - localPoint2.y
            length = sqrt((x * x + y * y).toDouble()).toFloat()
        }
        return length
    }

    /** Sets to recalculate the scaled length of this line. */
    fun setToRecalculateScaledLength() {
        calculateScaledLength = true
    }

    /**
     * Calculates and returns the scaled length of this line.
     *
     * @return The scaled length of this line.
     */
    fun getScaledLength(): Float {
        if (calculateScaledLength) {
            calculateScaledLength = false
            val x = (localPoint1.x - localPoint2.x) * scaleX
            val y = (localPoint1.y - localPoint2.y) * scaleY
            scaledLength = sqrt((x * x + y * y).toDouble()).toFloat()
        }
        return scaledLength
    }

    /**
     * Sets the first local point (unscaled, unrotated, etc.) of this line.
     *
     * @param point The first point of this line.
     * @return This shape for chaining
     */
    fun setFirstLocalPoint(point: Vector2) = setFirstLocalPoint(point.x, point.y)

    /**
     * Sets the first local point (unscaled, unrotated, etc.) of this line.
     *
     * @param x1 The x-coordinate of the first point of this line.
     * @param y1 The y-coordinate of the first point of this line.
     * @return This shape for chaining
     */
    fun setFirstLocalPoint(x1: Float, y1: Float): GameLine {
        val secondLocalPoint = getSecondLocalPoint()
        setLocalPoints(x1, y1, secondLocalPoint.x, secondLocalPoint.y)
        return this
    }

    /**
     * Sets the second local point (unscaled, unrotated, etc.) of this line.
     *
     * @param point The second point of this line.
     * @return This shape for chaining
     */
    fun setSecondLocalPoint(point: Vector2) = setSecondLocalPoint(point.x, point.y)

    /**
     * Sets the second local point (unscaled, unrotated, etc.) of this line.
     *
     * @param x2 The x-coordinate of the second point of this line.
     * @param y2 The y-coordinate of the second point of this line.
     * @return This shape for chaining
     */
    fun setSecondLocalPoint(x2: Float, y2: Float): GameLine {
        setLocalPoints(localPoint1.x, localPoint2.y, x2, y2)
        return this
    }

    /**
     * Sets the local points (unscaled, unrotated, etc.) of this line.
     *
     * @param x1 The x-coordinate of the first point of this line.
     * @param y1 The y-coordinate of the first point of this line.
     * @param x2 The x-coordinate of the second point of this line.
     * @param y2 The y-coordinate of the second point of this line.
     * @return This shape for chaining
     */
    fun setLocalPoints(x1: Float, y1: Float, x2: Float, y2: Float): GameLine {
        localPoint1.set(x1, y1)
        localPoint2.set(x2, y2)
        dirty = true
        calculateLength = true
        return this
    }

    /**
     * Sets the local points (unscaled, unrotated, etc.) of this line.
     *
     * @param point1 The first point of this line.
     * @param point2 The second point of this line.
     * @return This shape for chaining
     */
    fun setLocalPoints(point1: Vector2, point2: Vector2) = setLocalPoints(point1.x, point1.y, point2.x, point2.y)

    /**
     * Gets the first local point (unscaled, unrotated, etc.) of this line. The return value of this method should be
     * treated as read-only; if the values are modified, this line will NOT be set to dirty, which will lead to
     * unexpected behavior. Therefore, if the values are modified, then [setToDirty] should be called.
     *
     * @return The first local point of this line.
     */
    fun getFirstLocalPoint() = localPoint1

    /**
     * Gets the second local point (unscaled, unrotated, etc.) of this line. The return value of this method should be
     * treated as read-only; if the values are modified, this line will NOT be set to dirty, which will lead to
     * unexpected behavior. Therefore, if the values are modified, then [setToDirty] should be called.
     *
     * @return The second local point of this line.
     */
    fun getSecondLocalPoint() = localPoint2

    /**
     * Gets the world points (scaled, rotated, etc.) of this line. If a pair is supplied, then that pair is
     * populated rather than a new pair. The points returned in the pair are references to the internal world
     * point fields. Although these references can be modified, it is advisable to treat these are read-only
     * values; the references are changed each time [getWorldPoints] is called.
     *
     * @param pair the optional pair to provide (if one is not provided, then a new one is instantiated)
     * @return The world points of this line.
     */
    fun getWorldPoints(pair: GamePair<Vector2, Vector2>? = null): GamePair<Vector2, Vector2> {
        val pointsPair = pair?.set(worldPoint1, worldPoint2) ?: GamePair(worldPoint1, worldPoint2)

        if (!dirty) return pointsPair
        dirty = false

        val cos = MathUtils.cosDeg(rotation)
        val sin = MathUtils.sinDeg(rotation)

        var first = true
        forEachLocalPoint {
            var x = it.x - originX
            var y = it.y - originY

            x *= scaleX
            y *= scaleY

            if (rotation != 0f) {
                val oldX = x
                x = cos * x - sin * y
                y = sin * oldX + cos * y
            }

            val worldPoint = if (first) worldPoint1 else worldPoint2
            first = false

            worldPoint.x = position.x + x + originX
            worldPoint.y = position.y + y + originY
        }

        return pointsPair
    }

    /**
     * Applies the specified action to each of the local points. If the values are modified, then [setToDirty] should
     * be called manually.
     *
     * @param action the action
     * @return this line for chaining
     */
    fun forEachLocalPoint(action: (Vector2) -> Unit): GameLine {
        action.invoke(localPoint1)
        action.invoke(localPoint2)
        return this
    }

    /**
     * Returns the distance of the specified [point] from this [GameLine]. If [segment] is true, then the distance is
     * calculated using this line as a segment. Otherwise, the distance is calculated using this line as a continuous
     * (non-ending) line. World points are used for the calculation.
     *
     * @param point the point to calculate the distance from
     * @param segment whether to treat this line as a segment or a continuous line
     * @return the distance of the point from this line/segment
     */
    fun worldDistanceFromPoint(point: Vector2, segment: Boolean = true): Float {
        val worldPoints = getWorldPoints()
        return if (segment) Intersector.distanceSegmentPoint(worldPoints.first, worldPoints.second, point)
        else {
            val wp1 = worldPoints.first
            val wp2 = worldPoints.second
            Intersector.distanceLinePoint(wp1.x, wp1.y, wp2.x, wp2.y, point.x, point.y)
        }
    }

    /**
     * Gets the intersection point between this line and the other line, if there is one. If there is none,
     * then null is returned.
     *
     * @param line the other line
     * @return the intersection point, or null if there is none
     */
    fun intersectionPoint(line: GameLine): Vector2? {
        val thisWP = getWorldPoints()
        val otherWP = line.getWorldPoints()
        val intersection = Vector2()
        return if (Intersector.intersectLines(
                thisWP.first,
                thisWP.second,
                otherWP.first,
                otherWP.second,
                intersection
            )
        ) intersection else null
    }

    /**
     * Checks if the point is contained in this line. The world points are used for calculating the
     * containment via [getWorldPoints].
     *
     * @param point The point to check.
     * @return True if the point is contained in this line.
     */
    override fun contains(point: Vector2): Boolean {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        return Intersector.pointLineSide(
            _worldPoint1,
            _worldPoint2,
            point
        ) == 0 && point.x <= getMaxX() && point.x >= getX()
    }

    /**
     * Checks if the point is contained in this line. The world points are used for calculating the
     * containment via [getWorldPoints].
     *
     * @param x The first coordinate of the point to check.
     * @param y The second coordinate of the point to check.
     * @return True if the point is contained in this line.
     * @see contains
     */
    override fun contains(x: Float, y: Float) = contains(Vector2(x, y))

    /**
     * Draws this line using the world points.
     *
     * @return The drawer.
     */
    override fun draw(drawer: ShapeRenderer) {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        drawer.color = color
        drawer.set(shapeType)
        drawer.rectLine(_worldPoint1, _worldPoint2, thickness)
    }

    override fun setCenter(centerX: Float, centerY: Float): GameLine {
        val currentCenter = getCenter()
        val centerDeltaX = centerX - currentCenter.x
        val centerDeltaY = centerY - currentCenter.y

        if (centerDeltaX == 0f && centerDeltaY == 0f) return this

        position.x += centerDeltaX
        position.y += centerDeltaY

        dirty = true
        calculateLength = true

        return this
    }

    /**
     * Centers the world coordinates of the line on the given point.
     *
     * @param center The point to center the world coordinates of the line on.
     * @return This line.
     */
    override fun setCenter(center: Vector2) = setCenter(center.x, center.y)

    /**
     * Returns the center of the world points.
     *
     * @return The center of the world points.
     */
    override fun getCenter(): Vector2 {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        return Vector2((_worldPoint1.x + _worldPoint2.x) / 2f, (_worldPoint1.y + _worldPoint2.y) / 2f)
    }

    /**
     * Returns the center of the local points.
     *
     * @return The center of the local points.
     */
    fun getLocalCenter() = Vector2((localPoint1.x + localPoint2.x) / 2f, (localPoint1.y + localPoint2.y) / 2f)

    /**
     * Sets the x-coordinate of the first point of this line.
     *
     * @param x The new x-coordinate of the first point of this line.
     * @return This line.
     */
    override fun setX(x: Float): GameLine {
        if (position.x != x) {
            position.x = x
            dirty = true
        }
        return this
    }

    /**
     * Sets the y-coordinate of the first point of this line.
     *
     * @param y The new y-coordinate of the first point of this line.
     * @return This line.
     */
    override fun setY(y: Float): GameLine {
        if (position.y != y) {
            position.y = y
            dirty = true
        }
        return this
    }

    /**
     * Returns the x position of the line.
     *
     * @return The x position of the line.
     */
    override fun getX() = position.x

    /**
     * Returns the y position of the line.
     *
     * @return The y position of the line.
     */
    override fun getY() = position.y

    /**
     * Returns the max x of the line using the world points via [getWorldPoints].
     *
     * @return The max x of the line.
     */
    override fun getMaxX(): Float {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        return max(_worldPoint1.x, _worldPoint2.x)
    }

    /**
     * Returns the max y of the line using the world points via [getWorldPoints].
     *
     * @return The max y of the line.
     */
    override fun getMaxY(): Float {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()
        return max(_worldPoint1.y, _worldPoint2.y)
    }

    /**
     * Translates the position of this line.
     *
     * @param translateX The amount to translate the x-coordinate of this line.
     * @param translateY The amount to translate the y-coordinate of this line.
     * @return This line.
     */
    override fun translation(translateX: Float, translateY: Float): GameLine {
        position.x += translateX
        position.y += translateY
        dirty = true
        return this
    }

    /**
     * Returns the width of this line's bounding rectangle.
     *
     * @return the width of this line's bounding rectangle.
     */
    override fun getWidth() = getBoundingRectangle().width

    /**
     * Returns the height of this line's bounding rectangle.
     *
     * @return the height of this line's bounding rectangle.
     */
    override fun getHeight() = getBoundingRectangle().height

    /**
     * Sets the origin of this line.
     *
     * @param origin The origin.
     * @return This line.
     */
    fun setOrigin(origin: Vector2) = setOrigin(origin.x, origin.y)

    /**
     * Sets the origin of this line.
     *
     * @param originX The x-coordinate of the origin.
     * @param originY The y-coordinate of the origin.
     * @return This line.
     */
    fun setOrigin(originX: Float, originY: Float): GameLine {
        this.originX = originX
        this.originY = originY
        return this
    }

    /**
     * Returns a copy of this line.
     *
     * @return A copy of this line.
     */
    override fun copy(): GameLine = GameLine(this)

    /**
     * Returns true if the provided [IGameShape2D] overlaps this game line. The world points are used
     * for calculating the overlap via [getWorldPoints].
     *
     * @return True if the provided [IGameShape2D] overlaps this game line.
     */
    override fun overlaps(other: IGameShape2D): Boolean {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()

        return when (other) {
            is GameRectangle -> Intersector.intersectSegmentRectangle(_worldPoint1, _worldPoint2, other)
            is GameCircle -> Intersector.intersectSegmentCircle(
                _worldPoint1, _worldPoint2, other.getCenter(), other.getRadius() * other.getRadius()
            )

            is GameLine -> {
                val (otherWorldPoint1, otherWorldPoint2) = other.getWorldPoints()
                Intersector.intersectSegments(
                    _worldPoint1, _worldPoint2, otherWorldPoint1, otherWorldPoint2, null
                )
            }

            is GamePolygon -> Intersector.intersectLinePolygon(worldPoint1, worldPoint2, other.libgdxPolygon)
            else -> OVERLAP_EXTENSION?.invoke(this, other) ?: false
        }
    }

    /**
     * Returns the bounding rectangle of this line. The world points are used for calculating the
     * bounding rectangle via [getWorldPoints].
     *
     * @return The bounding rectangle of this line.
     */
    override fun getBoundingRectangle(bounds: GameRectangle?): GameRectangle {
        val (_worldPoint1, _worldPoint2) = getWorldPoints()

        val minX = min(_worldPoint1.x, _worldPoint2.x)
        val maxX = max(_worldPoint1.x, _worldPoint2.x)
        val minY = min(_worldPoint1.y, _worldPoint2.y)
        val maxY = max(_worldPoint1.y, _worldPoint2.y)

        val lineBounds = bounds ?: GameRectangle()
        return lineBounds.set(minX, minY, maxX - minX, maxY - minY)
    }

    override fun toString() = getWorldPoints().toString()
}
