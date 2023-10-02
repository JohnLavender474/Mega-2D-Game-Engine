package com.engine.world

import com.engine.components.IGameComponent

class BodyComponent(var body: Body) : IGameComponent {

  override fun reset() {
    body.reset()
  }
}
