package com.engine.common.interfaces

interface UpdateFunction<T, R> {
    fun apply(data: T, delta: Float): R
}