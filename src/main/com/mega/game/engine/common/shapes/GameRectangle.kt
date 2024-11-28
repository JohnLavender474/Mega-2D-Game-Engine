package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.FloatArray
import com.mega.game.engine.common.enums.Position
import com.mega.game.engine.common.extensions.gdxArrayOf
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.common.objects.Matrix
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.pairTo
import java.util.function.BiPredicate
import kotlin.math.*

open class GameRectangle() : IGameShape2D, IRotatableShape {

    companion object {

        private var OVERLAP_EXTENSION: ((GameRectangle, IGameShape2D) -> Boolean)? = null

        fun setOverlapExtension(overlapExtension: (GameRectangle, IGameShape2D) -> Boolean) {
            OVERLAP_EXTENSION = overlapExtension
        }

        fun setOverlapExtension(overlapExtension: BiPredicate<GameRectangle, IGameShape2D>) {
            setOverlapExtension { rect, shape -> overlapExtension.test(rect, shape) }
        }

        fun calculateBoundsFromLines(lines: Array<GamePair<Vector2, Vector2>>, out: GameRectangle): GameRectangle {
            var minX = Float.MAX_VALUE
            var minY = Float.MAX_VALUE
            var maxX = Float.NEGATIVE_INFINITY
            var maxY = Float.NEGATIVE_INFINITY

            lines.forEach {
                val (point1, point2) = it
                minX = min(minX, min(point1.x, point2.x))
                minY = min(minY, min(point1.y, point2.y))
                maxX = max(maxX, max(point1.x, point2.x))
                maxY = max(maxY, max(point1.y, point2.y))
            }

            return out.set(minX, minY, abs(maxX - minX), abs(maxY - minY))
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
    var width: Float
        get() = getWidth()
        set(value) {
            setWidth(value)
        }
    var height: Float
        get() = getHeight()
        set(value) {
            setHeight(value)
        }

    internal val rectangle = Rectangle()

    private val reusableLinesArray = gdxArrayOf(
        GameLine(),
        GameLine(),
        GameLine(),
        GameLine()
    )
    private val reusablePointsArray = gdxArrayOf(
        Vector2() pairTo Vector2(),
        Vector2() pairTo Vector2(),
        Vector2() pairTo Vector2(),
        Vector2() pairTo Vector2(),
    )
    private val reusablePolygon = GamePolygon()
    private val out1 = Vector2()
    private val out2 = Vector2()

    constructor(x: Number, y: Number, width: Number, height: Number) : this() {
        set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    }

    constructor(rect: Rectangle) : this() {
        set(rect)
    }

    constructor(rect: GameRectangle) : this() {
        set(rect)
    }

    override fun getProps(out: Properties): Properties {
        out.putAll(
            "x" pairTo getX(),
            "y" pairTo getY(),
            "width" pairTo getWidth(),
            "height" pairTo getHeight(),
        )
        return out
    }

    override fun setWithProps(props: Properties): IGameShape2D {
        setX(props.getOrDefault("x", getX(), Float::class))
        setY(props.getOrDefault("y", getY(), Float::class))
        setWidth(props.getOrDefault("width", getWidth(), Float::class))
        setHeight(props.getOrDefault("height", getHeight(), Float::class))
        return this
    }

    fun set(rect: GameRectangle): GameRectangle {
        set(rect.rectangle)
        return this
    }

    fun get(out: Rectangle): Rectangle = out.set(rectangle)

    fun getAsLines(
        out: Array<GameLine>,
        topLine: GameLine,
        bottomLine: GameLine,
        leftLine: GameLine,
        rightLine: GameLine,
    ): Array<GameLine> {
        out.addAll(
            topLine.set(getTopLeftPoint(out1), getTopRightPoint(out2)),
            bottomLine.set(getBottomLeftPoint(out1), getBottomRightPoint(out2)),
            leftLine.set(getBottomLeftPoint(out1), getTopLeftPoint(out2)),
            rightLine.set(getBottomRightPoint(out1), getTopRightPoint(out2))
        )
        return out
    }

    fun getVertices(out: FloatArray): FloatArray {
        out.addAll(
            getX(),
            getY(),
            getX() + getWidth(),
            getY(),
            getX() + getWidth(),
            getY() + getHeight(),
            getX(),
            getY() + getHeight()
        )
        return out
    }

    override fun setRotation(rotation: Float, originX: Float, originY: Float) {
        val line1 = reusableLinesArray[0]
        val line2 = reusableLinesArray[1]
        val line3 = reusableLinesArray[2]
        val line4 = reusableLinesArray[3]
        reusableLinesArray.clear()
        getAsLines(reusableLinesArray, line1, line2, line3, line4)

        for (i in 0 until reusableLinesArray.size) {
            val line = reusableLinesArray[i]
            line.originX = originX
            line.originY = originY
            line.rotation = rotation
            val pair = reusablePointsArray[i]
            line.calculateWorldPoints(pair.first, pair.second)
        }

        calculateBoundsFromLines(reusablePointsArray, this)
    }

    fun setSize(sizeXY: Float): GameRectangle {
        rectangle.setSize(sizeXY)
        return this
    }

    fun setSize(width: Float, height: Float): GameRectangle {
        rectangle.setSize(width, height)
        return this
    }

    fun setSize(size: Vector2) = setSize(size.x, size.y)

    fun set(x: Float, y: Float, width: Float, height: Float): GameRectangle {
        rectangle.set(x, y, width, height)
        return this
    }

    fun set(rect: Rectangle): GameRectangle {
        rectangle.set(rect)
        return this
    }

    override fun setX(x: Float): GameRectangle {
        rectangle.x = x
        return this
    }

    override fun setY(y: Float): GameRectangle {
        rectangle.y = y
        return this
    }

    override fun getX() = rectangle.x

    override fun getY() = rectangle.y

    fun setWidth(width: Float): GameRectangle {
        rectangle.width = width
        return this
    }

    fun setHeight(height: Float): GameRectangle {
        rectangle.height = height
        return this
    }

    override fun getPosition(out: Vector2): Vector2 = out.set(getX(), getY())

    override fun setPosition(x: Float, y: Float): GameRectangle {
        rectangle.setPosition(x, y)
        return this
    }

    fun merge(rect: Rectangle): GameRectangle {
        rectangle.merge(rect)
        return this
    }

    fun merge(x: Float, y: Float): GameRectangle {
        rectangle.merge(x, y)
        return this
    }

    fun merge(vec: Vector2): GameRectangle {
        rectangle.merge(vec)
        return this
    }

    fun merge(vecs: kotlin.Array<Vector2>): GameRectangle {
        rectangle.merge(vecs)
        return this
    }

    fun fitOutside(rect: Rectangle): GameRectangle {
        rectangle.fitOutside(rect)
        return this
    }

    fun fitOutside(rect: GameRectangle) = fitOutside(rect.rectangle)

    fun fitInside(rect: Rectangle): GameRectangle {
        rectangle.fitInside(rect)
        return this
    }

    fun fitInside(rect: GameRectangle) = fitInside(rect.rectangle)

    fun fromString(v: String): GameRectangle {
        rectangle.fromString(v)
        return this
    }

    fun setMaxX(maxX: Float) = setX(maxX - getWidth())

    fun setMaxY(maxY: Float) = setY(maxY - getHeight())

    override fun getMaxX() = getX() + getWidth()

    override fun getMaxY() = getY() + getHeight()

    override fun overlaps(other: IGameShape2D) =
        when (other) {
            is GameRectangle -> Intersector.overlaps(rectangle, other.rectangle)
            is GameCircle -> Intersector.overlaps(other.libgdxCircle, rectangle)
            is GameLine -> {
                other.calculateWorldPoints(out1, out2)
                Intersector.intersectSegmentRectangle(out1, out2, rectangle)
            }

            is GamePolygon -> Intersector.overlapConvexPolygons(
                toPolygon(reusablePolygon).libgdxPolygon,
                other.libgdxPolygon
            )

            else -> OVERLAP_EXTENSION?.invoke(this, other) == true
        }

    override fun getBoundingRectangle(out: GameRectangle) = out.set(this)

    override fun setCenter(centerX: Float, centerY: Float): GameRectangle {
        setCenterX(centerX)
        setCenterY(centerY)
        return this
    }

    override fun getCenter(out: Vector2): Vector2 = rectangle.getCenter(out)

    override fun setCenter(center: Vector2): GameRectangle = setCenter(center.x, center.y)

    fun setCenterX(centerX: Float): GameRectangle {
        setCenter(centerX, getCenter(out1).y)
        return this
    }

    fun setCenterY(centerY: Float): GameRectangle {
        setCenter(getCenter(out1).x, centerY)
        return this
    }

    override fun translate(translateX: Float, translateY: Float): GameRectangle {
        setX(getX() + translateX)
        setY(getY() + translateY)
        return this
    }

    override fun getWidth(): Float {
        TODO("Not yet implemented")
    }

    override fun getHeight(): Float {
        TODO("Not yet implemented")
    }

    fun positionOnPoint(point: Vector2, position: Position): GameRectangle {
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

    fun getPositionPoint(position: Position, out: Vector2) = when (position) {
        Position.TOP_LEFT -> getTopLeftPoint(out)
        Position.TOP_CENTER -> getTopCenterPoint(out)
        Position.TOP_RIGHT -> getTopRightPoint(out)
        Position.CENTER_LEFT -> getCenterLeftPoint(out)
        Position.CENTER -> getCenterPoint(out)
        Position.CENTER_RIGHT -> getCenterRightPoint(out)
        Position.BOTTOM_LEFT -> getBottomLeftPoint(out)
        Position.BOTTOM_CENTER -> getBottomCenterPoint(out)
        Position.BOTTOM_RIGHT -> getBottomRightPoint(out)
    }

    override fun copy(): GameRectangle = GameRectangle(this)

    fun setTopLeftToPoint(topLeftPoint: Vector2): GameRectangle =
        setPosition(topLeftPoint.x, topLeftPoint.y - getHeight())

    fun getTopLeftPoint(out: Vector2): Vector2 = out.set(getX(), getY() + getHeight())

    fun setTopCenterToPoint(topCenterPoint: Vector2): GameRectangle =
        setPosition(topCenterPoint.x - (getWidth() / 2f), topCenterPoint.y - getHeight())

    fun getTopCenterPoint(out: Vector2): Vector2 = out.set(getX() + (getWidth() / 2f), getY() + getHeight())

    fun setTopRightToPoint(topRightPoint: Vector2): GameRectangle =
        setPosition(topRightPoint.x - getWidth(), topRightPoint.y - getHeight())

    fun getTopRightPoint(out: Vector2): Vector2 = out.set(getX() + getWidth(), getY() + getHeight())

    fun setCenterLeftToPoint(centerLeftPoint: Vector2): GameRectangle =
        this.setPosition(centerLeftPoint.x, centerLeftPoint.y - (getHeight() / 2f))

    fun getCenterLeftPoint(out: Vector2): Vector2 = out.set(getX(), getY() + (getHeight() / 2f))

    fun setCenterToPoint(centerPoint: Vector2): GameRectangle = this.setCenter(centerPoint)

    fun getCenterPoint(out: Vector2): Vector2 = this.getCenter(out)

    fun setCenterRightToPoint(centerRightPoint: Vector2): GameRectangle =
        this.setPosition(centerRightPoint.x - this.getWidth(), centerRightPoint.y - (this.getHeight() / 2f))

    fun getCenterRightPoint(out: Vector2): Vector2 =
        out.set(this.getX() + this.getWidth(), this.getY() + (this.getHeight() / 2f))

    fun setBottomLeftToPoint(bottomLeftPoint: Vector2): GameRectangle {
        this.setPosition(bottomLeftPoint)
        return this
    }

    fun getBottomLeftPoint(out: Vector2): Vector2 = out.set(this.getX(), this.getY())

    fun setBottomCenterToPoint(bottomCenterPoint: Vector2): GameRectangle =
        this.setPosition(bottomCenterPoint.x - this.getWidth() / 2f, bottomCenterPoint.y)

    fun getBottomCenterPoint(out: Vector2): Vector2 = out.set(this.getX() + this.getWidth() / 2f, this.getY())

    fun setBottomRightToPoint(bottomRightPoint: Vector2): GameRectangle =
        this.setPosition(bottomRightPoint.x - this.getWidth(), bottomRightPoint.y)

    fun getBottomRightPoint(out: Vector2): Vector2 = out.set(this.getX() + this.getWidth(), this.getY())

    fun toPolygon(out: GamePolygon): GamePolygon {
        out.setLocalVertices(floatArrayOf(0f, 0f, getWidth(), 0f, getWidth(), getHeight(), 0f, getHeight()))
        out.setPosition(getX(), getY())
        return out
    }

    fun getSplitDimensions(size: Float, out: GamePair<Int, Int>) = getSplitDimensions(size, size, out)

    fun getSplitDimensions(rectWidth: Float, rectHeight: Float, out: GamePair<Int, Int>): GamePair<Int, Int> {
        val rows = (getHeight() / rectHeight).roundToInt()
        val columns = (getWidth() / rectWidth).roundToInt()
        return out.set(rows, columns)
    }

    fun splitIntoCells(rowsAndColumns: Int, out: Matrix<GameRectangle>) =
        splitIntoCells(rowsAndColumns, rowsAndColumns, out)


    fun splitIntoCells(rows: Int, columns: Int, out: Matrix<GameRectangle>): Matrix<GameRectangle> {
        val cellWidth = ceil(getWidth() / columns).toInt()
        val cellHeight = ceil(getHeight() / rows).toInt()

        out.clear()
        out.rows = rows
        out.columns = columns

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val cell = GameRectangle(getX() + column * cellWidth, getY() + row * cellHeight, cellWidth, cellHeight)
                out[column, row] = cell
            }
        }

        return out
    }

    fun splitByCellSize(size: Float, out: Matrix<GameRectangle>) = splitByCellSize(size, size, out)

    fun splitByCellSize(rectWidth: Float, rectHeight: Float, out: Matrix<GameRectangle>): Matrix<GameRectangle> {
        val rows = ceil(getHeight() / rectHeight).roundToInt()
        val columns = ceil(getWidth() / rectWidth).roundToInt()
        return splitIntoCells(rows, columns, out)
    }

    override fun contains(point: Vector2) = rectangle.contains(point)

    override fun contains(x: Float, y: Float) = rectangle.contains(x, y)

    override fun equals(other: Any?) = other is GameRectangle && rectangle == other.rectangle

    override fun hashCode() = rectangle.hashCode()

    override fun toString() = rectangle.toString()
}
