package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.objects.Properties

interface IGameShape2D : Shape2D, ICopyable<IGameShape2D> {

    fun getProps(out: Properties): Properties

    fun setWithProps(props: Properties): IGameShape2D

    fun overlaps(other: IGameShape2D): Boolean

    fun getBoundingRectangle(out: GameRectangle): GameRectangle

    fun setPosition(position: Vector2) = setPosition(position.x, position.y)

    fun setPosition(x: Float, y: Float): IGameShape2D {
        setX(x)
        setY(y)
        return this
    }

    fun setX(x: Float): IGameShape2D

    fun setY(y: Float): IGameShape2D

    fun getX(): Float

    fun getY(): Float

    fun getPosition(out: Vector2): Vector2 = out.set(getX(), getY())

    fun getCenter(out: Vector2): Vector2

    fun setCenter(center: Vector2): IGameShape2D

    fun setCenter(centerX: Float, centerY: Float): IGameShape2D

    fun getMaxX(): Float

    fun getMaxY(): Float

    fun translate(translateX: Float, translateY: Float): IGameShape2D

    fun translate(translation: Vector2) = translate(translation.x, translation.y)

    fun getWidth(): Float

    fun getHeight(): Float
}
