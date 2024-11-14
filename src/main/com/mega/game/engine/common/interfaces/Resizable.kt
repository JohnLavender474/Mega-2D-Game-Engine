package com.mega.game.engine.common.interfaces

/**
 * Interface for resizable objects.
 */
interface Resizable {

    /**
     * Resizes this object.
     *
     * @param width the width
     * @param height the height
     */
    fun resize(width: Number, height: Number)
}