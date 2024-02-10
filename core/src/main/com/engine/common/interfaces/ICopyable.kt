package com.engine.common.interfaces

/** An object that can be copied. */
interface ICopyable {

    /**
     * Creates a copy of this object.
     *
     * @return a copy of this object
     */
    fun copy(): ICopyable
}
