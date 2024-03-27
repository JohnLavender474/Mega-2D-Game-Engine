package com.engine.common.interfaces

import com.engine.common.shapes.GameRectangle

/**
 * Interface for objects that have bounds.
 */
interface IBoundsSupplier {

    /**
     * Gets the bounds of this object.
     *
     * @return the bounds of this object
     */
    fun getBounds(): GameRectangle
}