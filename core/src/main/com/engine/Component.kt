package com.engine

import com.engine.common.interfaces.Resettable

interface Component : Resettable {
    override fun reset() {}
}