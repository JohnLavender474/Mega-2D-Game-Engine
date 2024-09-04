package com.mega.game.engine.common.interfaces

import com.badlogic.gdx.math.Vector2

/** Interface for objects that have a position. */
interface IPositionSupplier {

    /**
     * Gets the first-coordinate of this object.
     *
     * @return the first-coordinate of this object
     */
    fun getX(): Float

    /**
     * Gets the second-coordinate of this object.
     *
     * @return the second-coordinate of this object
     */
    fun getY(): Float

    /**
     * Gets the position of this object.
     *
     * @return the position of this object
     */
    fun getPosition() = Vector2(getX(), getY())
}
