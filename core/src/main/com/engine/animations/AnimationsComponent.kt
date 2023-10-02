package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.ObjectMap
import com.engine.components.IGameComponent

// TODO: should this component reset all animations?
class AnimationsComponent(
    val animations: ObjectMap<Sprite, IAnimator> = ObjectMap(),
) : IGameComponent {

  constructor(
      sprite: Sprite,
      animator: IAnimator
  ) : this(ObjectMap<Sprite, IAnimator>().apply { put(sprite, animator) })
}
