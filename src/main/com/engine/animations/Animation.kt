package com.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.engine.common.extensions.gdxFilledArrayOf
import com.engine.drawables.sprites.splitAndFlatten

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
    internal val durations: Array<Float>

    private var loop = true

    internal var currentIndex = 0
        private set

    internal var elapsedTime = 0f
        private set

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
     * @param duration the duration to display each split region
     */
    constructor(
        region: TextureRegion,
        rows: Int,
        columns: Int,
        duration: Float,
        loop: Boolean = true
    ) : this(region, rows, columns, gdxFilledArrayOf(rows * columns, duration), loop)

    /**
     * Creates an animation with the specified texture region, rows, columns, and duration. The
     * animation will loop by default.
     *
     * @param region the texture region to animate
     * @param rows the number of rows to split the texture region into
     * @param columns the number of columns to split the texture region into
     * @param durations the durations to display each split region
     */
    constructor(
        region: TextureRegion,
        rows: Int,
        columns: Int,
        durations: Array<Float>,
        loop: Boolean = true
    ) {
        if (rows <= 0) throw IllegalArgumentException("The number of rows must be greater than 0")
        if (columns <= 0) throw IllegalArgumentException("The number of columns must be greater than 0")
        if (durations.size != rows * columns)
            throw IllegalArgumentException(
                "The number of durations must equal the number of rows times the number of columns. " +
                        "Expected ${rows * columns} durations but found ${durations.size} durations."
            )

        this.durations = durations
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
        this.durations = Array(animation.durations)
        loop = animation.loop
        if (reverse) {
            this.animation.reverse()
            this.durations.reverse()
        }
    }

    override fun getCurrentRegion(): TextureRegion = animation[currentIndex]

    override fun isFinished() = !loop && elapsedTime >= getDuration()

    override fun getDuration() = durations.sum()

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
        while (tempIndex < animation.size && currentLoopDuration > durations[tempIndex]) {
            currentLoopDuration -= durations[tempIndex]
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

    /**
     * Returns a new reversed animation from this one.
     *
     * @return a new reversed animation from this one
     */
    fun reversed() = Animation(this, true)
}
