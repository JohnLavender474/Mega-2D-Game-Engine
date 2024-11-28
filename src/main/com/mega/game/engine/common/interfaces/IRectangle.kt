package com.mega.game.engine.common.interfaces

import com.mega.game.engine.world.body.BodyType

interface IRectangle: IPositional, ICenterable, ISizable {

    var type: BodyType

    fun set(x: Float, y: Float, width: Float, height: Float) {
        setPosition(x, y)
        setSize(width, height)
    }
}