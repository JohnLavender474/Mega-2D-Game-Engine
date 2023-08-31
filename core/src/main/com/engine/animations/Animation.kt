package com.engine.animations

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.extensions.splitAndFlatten

/**
 * An animation that can be used to animate a texture region. The animation is created by splitting
 * the specified texture region into rows and columns and then storing the split regions in an
 * array. The animation is then played by iterating through the array of split regions and
 * displaying each region for the specified duration.
 *
 * @see IAnimation
 */
class Animation : IAnimation {

  private val animation: Array<TextureRegion>
  private val durations: FloatArray

  private var loop = true

  internal var currentIndex: Int = 0
    private set
  internal var elapsedTime = 0f
    private set

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
  ) : this(region, rows, columns, FloatArray(rows * columns) { duration }, loop)

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
      durations: FloatArray,
      loop: Boolean = true
  ) {
    if (rows <= 0) {
      throw IllegalArgumentException("The number of rows must be greater than 0")
    }
    if (columns <= 0) {
      throw IllegalArgumentException("The number of columns must be greater than 0")
    }
    if (durations.size != rows * columns) {
      throw IllegalArgumentException(
          "The number of durations must equal the number of rows times the number of columns")
    }
    this.durations = durations
    this.animation = region.splitAndFlatten(rows, columns)
    this.loop = loop
  }

  override fun getCurrentRegion() = animation[currentIndex]

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
    if (elapsedTime >= duration) {
      if (loop) {
        elapsedTime -= duration
      } else {
        elapsedTime = duration
        currentIndex = animation.size - 1
        return
      }
    }

    // If the animation is looping, then find the current region by subtracting the
    // duration of each region from the elapsed time until the elapsed time is less
    // than the duration of the current region
    var currentLoopDuration = elapsedTime
    println("currentLoopDuration: $currentLoopDuration")
    var tempIndex = 0
    while (currentLoopDuration > durations[tempIndex] && tempIndex < animation.size) {
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
}
