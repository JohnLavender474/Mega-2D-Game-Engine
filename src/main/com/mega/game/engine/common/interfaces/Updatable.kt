package com.mega.game.engine.common.interfaces

/** An interface for objects that can be updated. */
fun interface Updatable {

    /** Updates this object. */
    fun update(delta: Float)
}
