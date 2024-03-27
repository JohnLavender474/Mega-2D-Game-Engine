package com.engine.common.interfaces

/** An object that can be copied. */
interface ICopyable<T> {

    /**
     * Creates a copy of this object.
     *
     * @return a copy of this object
     */
    fun copy(): T
}
