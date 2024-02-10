package com.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An animation that can be used to animate a texture region. */
interface IAnimation : Updatable, Resettable {

    /**
     * Returns the current texture region of this animation.
     *
     * @return the current texture region of this animation
     */
    fun getCurrentRegion(): TextureRegion?

    /**
     * Returns whether this animation is finished.
     *
     * @return whether this animation is finished
     */
    fun isFinished(): Boolean

    /**
     * Returns the duration of this animation.
     *
     * @return the duration of this animation
     */
    fun getDuration(): Float

    /**
     * Returns whether this animation is looping.
     *
     * @return whether this animation is looping
     */
    fun isLooping(): Boolean

    /**
     * Sets whether this animation is looping.
     *
     * @param loop whether this animation is looping
     * @return this animation for chaining
     */
    fun setLooping(loop: Boolean)
}
