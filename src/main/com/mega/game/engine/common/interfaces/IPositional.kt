package com.mega.game.engine.common.interfaces


interface IPositional : IPositionSupplier {


    fun setPosition(x: Float, y: Float)


    fun translate(x: Float, y: Float) = setPosition(getX() + x, getY() + y)
}
