package com.mega.game.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.extensions.gdxFilledArrayOf
import com.mega.game.engine.drawables.sprites.splitAndFlatten

/**
 * An animation that can be used to animate a texture region. The animation is created by splitting
 * the specified texture region into rows and columns and then storing the split regions in an
 * array. The animation is then played by iterating through the array of split regions and
 * displaying each region for the specified duration.
 *
 * @see IAnimation
 */
class Animation : IAnimation {

    internal val animation: Array<TextureRegion>
    internal val frameDurations: Array<Float>
    internal var currentIndex = 0
        private set
    internal var elapsedTime = 0f
        private set

    private var loop = true

    /**
     * Creates an animation with the specified texture region. The rows and columns values are each
     * set to 1 with this constructor. The animation will loop by default.
     *
     * @param region the texture region to animate
     */
    constructor(region: TextureRegion) : this(region, true)

    /**
     * Creates an animation with the specified texture region. The rows and columns values are each
     * set to 1 with this constructor.
     *
     * @param region the texture region to animate
     * @param loop whether the animation should loop
     */
    constructor(region: TextureRegion, loop: Boolean) : this(region, 1, 1, 1f, loop)

    /**
     * Creates an animation with the specified texture region, rows, columns, and duration. The
     * animation will loop by default.
     *
     * @param region the texture region to animate
     * @param rows the number of rows to split the texture region into
     * @param columns the number of columns to split the texture region into
     * @param frameDuration the duration to display each split region
     */
    constructor(
        region: TextureRegion, rows: Int, columns: Int, frameDuration: Float, loop: Boolean = true
    ) : this(region, rows, columns, gdxFilledArrayOf(rows * columns, frameDuration), loop)

    /**
     * Creates an animation with the specified texture region, rows, columns, and duration. The
     * animation will loop by default.
     *
     * @param region the texture region to animate
     * @param rows the number of rows to split the texture region into
     * @param columns the number of columns to split the texture region into
     * @param frameDurations the durations to display each split region
     */
    constructor(
        region: TextureRegion, rows: Int, columns: Int, frameDurations: Array<Float>, loop: Boolean = true
    ) {
        if (rows <= 0) throw IllegalArgumentException("The number of rows must be greater than 0")
        if (columns <= 0) throw IllegalArgumentException("The number of columns must be greater than 0")
        if (frameDurations.size != rows * columns) throw IllegalArgumentException(
            "The number of durations must equal the number of rows times the number of columns. " + "Expected ${rows * columns} durations but found ${frameDurations.size} durations."
        )

        this.frameDurations = frameDurations
        this.animation = region.splitAndFlatten(rows, columns)
        this.loop = loop
    }

    /**
     * Creates a new animation from the provided animation. If [reverse] is true, then the animation
     * will be reversed.
     *
     * @param animation the animation to copy
     * @param reverse whether to reverse the animation
     */
    constructor(animation: Animation, reverse: Boolean = false) {
        this.animation = Array(animation.animation)
        this.frameDurations = Array(animation.frameDurations)
        loop = animation.loop
        if (reverse) {
            this.animation.reverse()
            this.frameDurations.reverse()
        }
    }

    override fun getCurrentRegion(): TextureRegion = animation[currentIndex]

    override fun isFinished() = !loop && elapsedTime >= getDuration()

    override fun getDuration() = frameDurations.sum()

    override fun setFrameDuration(frameDuration: Float) {
        for (i in 0 until frameDurations.size) frameDurations[i] = frameDuration
    }

    override fun setFrameDuration(index: Int, frameDuration: Float) {
        if (index < 0 || index >= frameDurations.size) throw IllegalArgumentException(
            "The index must be greater than or equal to 0 and less than the size of the animation"
        )
        frameDurations[index] = frameDuration
    }

    override fun isLooping() = loop

    override fun setLooping(loop: Boolean) {
        this.loop = loop
    }

    /**
     * Updates the animation by iterating through the array of split regions and displaying each
     * region for the specified duration. If the animation is not looping, then the animation will
     * finish after the last split region is displayed.
     *
     * @param delta the time in seconds since the last update
     */
    override fun update(delta: Float) {
        elapsedTime += delta

        // If the animation is finished and not looping, then keep the elapsed time
        // at the duration, and set the current region to the last one (instead of the first)
        val duration = getDuration()
        while (elapsedTime >= duration) {
            if (loop) elapsedTime -= duration
            else {
                elapsedTime = duration
                currentIndex = animation.size - 1
                return
            }
        }

        // If the animation is looping, then find the current region by subtracting the
        // duration of each region from the elapsed time until the elapsed time is less
        // than the duration of the current region
        var currentLoopDuration = elapsedTime
        var tempIndex = 0
        while (tempIndex < animation.size && currentLoopDuration > frameDurations[tempIndex]) {
            currentLoopDuration -= frameDurations[tempIndex]
            tempIndex++
        }
        currentIndex = tempIndex
    }

    /**
     * Resets the animation by setting the elapsed time to 0 and setting the finished flag to false.
     * This will cause the animation to start from the beginning the next time it is updated. This
     * method should be called when the animation is finished and needs to be restarted.
     */
    override fun reset() {
        elapsedTime = 0f
    }

    override fun copy() = Animation(this)

    /**
     * Returns a copy of this animation reversed.
     *
     * @return a copy of this animation reversed
     */
    override fun reversed() = Animation(this, true)

    override fun slice(start: Int, end: Int): IAnimation {
        if (start < 0 || start >= animation.size) throw IllegalArgumentException("The start index must be greater than or equal to 0 and less than the size of the animation")
        if (end < 0 || end > animation.size) throw IllegalArgumentException("The end index must be greater than or equal to 0 and less than or equal to the size of the animation")
        if (start >= end) throw IllegalArgumentException("The start index must be less than the end index")

        val newAnimation = Animation(this)
        newAnimation.animation.clear()
        newAnimation.frameDurations.clear()
        for (i in start until end) {
            newAnimation.animation.add(animation[i])
            newAnimation.frameDurations.add(frameDurations[i])
        }
        return newAnimation
    }

    override fun setIndex(index: Int) {
        currentIndex = if (index < 0) 0 else if (index >= animation.size) animation.size - 1 else index
        elapsedTime = 0f
        for (i in 0 until currentIndex) elapsedTime += frameDurations[i]
    }

    override fun getIndex() = currentIndex

    override fun setCurrentTime(time: Float) {
        if (time < 0f) throw IllegalArgumentException("Time value cannot be less than zero")
        val duration = getDuration()
        elapsedTime = if (loop) time % duration else time.coerceAtMost(duration)
        if (!loop && elapsedTime == duration) {
            currentIndex = animation.size - 1
            return
        }

        var currentLoopDuration = elapsedTime
        var tempIndex = 0
        while (tempIndex < animation.size && currentLoopDuration > frameDurations[tempIndex]) {
            currentLoopDuration -= frameDurations[tempIndex]
            tempIndex++
        }
        currentIndex = tempIndex
    }

    override fun getCurrentTime() = elapsedTime
}
