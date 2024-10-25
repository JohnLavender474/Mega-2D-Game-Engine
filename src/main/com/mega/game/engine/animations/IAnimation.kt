package com.mega.game.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable

/** An animation that can be used to animate a texture region. */
interface IAnimation : ICopyable<IAnimation>, Updatable, Resettable {

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
     * Returns the total duration of this animation.
     *
     * @return the total duration of this animation
     */
    fun getDuration(): Float

    /**
     * Sets the duration of each frame of this animation.
     *
     * @param frameDuration the duration of each frame of this animation
     */
    fun setFrameDuration(frameDuration: Float)

    /**
     * Sets the duration of the frame at the specified [index] of this animation.
     *
     * @param index the index of the frame
     */
    fun setFrameDuration(index: Int, frameDuration: Float)

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

    /**
     * Creates a new reversed animation from this animation and returns it.
     *
     * @return the reversed animation
     */
    fun reversed(): IAnimation

    /**
     * Creates a new sliced animation from the specified [start] index (inclusive) to the specified [end] index
     * (exclusive) and returns it.
     *
     * @param start the start index (inclusive)
     * @param end the end index (exclusive)
     * @return the sliced animation
     */
    fun slice(start: Int, end: Int): IAnimation

    /**
     * Sets the index of this animation.
     *
     * @param index the index to set this animation to
     */
    fun setIndex(index: Int)

    /**
     * Gets the index of this animation.
     *
     * @return the index of this animation
     */
    fun getIndex(): Int

    /**
     * Sets the current time of this animation.
     *
     * @param time the time to set this animation to
     */
    fun setCurrentTime(time: Float)

    /**
     * Gets the current time of this animation.
     *
     * @return the time of this animation
     */
    fun getCurrentTime(): Float
}
