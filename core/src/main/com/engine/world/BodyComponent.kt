package com.engine.world

import com.engine.Component

class BodyComponent(var body: Body) : Component {

    override fun reset() {
        body.reset()
    }

}