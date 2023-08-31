package com.engine.animations

import com.badlogic.gdx.graphics.g2d.Sprite
import com.engine.GameComponent

class AnimationComponent(
    val animations: HashMap<Sprite, IAnimator> = HashMap(),
) : GameComponent {

  constructor(sprite: Sprite, animator: IAnimator) : this(hashMapOf(sprite to animator))
}
