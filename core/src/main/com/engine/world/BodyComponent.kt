package com.engine.world

import com.engine.Component

class BodyComponent(var body: Body? = null) : Component {

    override fun reset() {
        body?.reset()
    }

}