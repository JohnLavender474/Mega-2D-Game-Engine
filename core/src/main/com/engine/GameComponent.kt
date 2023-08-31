package com.engine

import com.engine.common.interfaces.Resettable

interface GameComponent : Resettable {
    override fun reset() {}
}