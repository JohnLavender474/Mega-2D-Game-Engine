package com.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An animation that can be used to animate a texture region. */
interface IAnimation : Updatable, Resettable {

  fun getCurrentRegion(): TextureRegion?

  fun isFinished(): Boolean

  fun getDuration(): Float

  fun isLooping(): Boolean

  fun setLooping(loop: Boolean)
}
