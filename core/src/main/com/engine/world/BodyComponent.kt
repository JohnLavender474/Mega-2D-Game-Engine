package com.engine.world

import com.engine.GameComponent

class BodyComponent(var body: Body) : GameComponent {

  override fun reset() {
    body.reset()
  }
}
