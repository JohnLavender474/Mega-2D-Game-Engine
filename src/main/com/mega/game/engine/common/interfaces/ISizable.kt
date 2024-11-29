package com.mega.game.engine.common.interfaces

import com.badlogic.gdx.math.Vector2

interface ISizable {

    fun getWidth(): Float

    fun getHeight(): Float

    fun setSize(size: Float) = setSize(size, size)

    fun setSize(width: Float, height: Float) {
        setWidth(width)
        setHeight(height)
    }

    fun getSize(out: Vector2): Vector2 = out.set(getWidth(), getHeight())

    fun setWidth(width: Float)

    fun setHeight(height: Float)

    fun translateSize(width: Float, height: Float) = setSize(getWidth() + width, getHeight() + height)
}
