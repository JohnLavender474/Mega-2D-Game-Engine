package com.mega.game.engine.common.interfaces

import com.badlogic.gdx.math.Vector2

interface ICenterable {

    fun setCenter(x: Float, y: Float)

    fun getCenter(out: Vector2): Vector2
}