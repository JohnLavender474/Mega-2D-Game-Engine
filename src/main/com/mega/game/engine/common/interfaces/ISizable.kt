package com.mega.game.engine.common.interfaces


interface ISizable {

    
    fun getWidth(): Float

    
    fun getHeight(): Float

    
    fun setSize(width: Float, height: Float) {
        setWidth(width)
        setHeight(height)
    }

    
    fun setWidth(width: Float)

    
    fun setHeight(height: Float)

    
    fun translateSize(width: Float, height: Float) = setSize(getWidth() + width, getHeight() + height)
}
