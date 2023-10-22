package com.engine.common.time

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import kotlin.math.min

/**
 * A timer that can be used to keep track of time and run [TimeMarkedRunnable]s at specific times.
 * The timer can be reset and will run the [TimeMarkedRunnable]s again.
 *
 * @param duration the duration of the timer
 */
class Timer(val duration: Float) : Updatable, Resettable {

  internal var runnables: Array<TimeMarkedRunnable> = Array()
  internal var runnableQueue = Queue<TimeMarkedRunnable>()

  var time = 0f
    private set

  var justFinished = false
    private set

  var runOnFinished: Runnable? = null

  /**
   * Creates a [Timer] with the given [duration] and [runOnFinished] [Runnable].
   *
   * @param duration the duration of the timer
   * @param _runOnFinished the [Runnable] to run when the timer finishes
   */
  constructor(duration: Float, _runOnFinished: Runnable) : this(duration) {
    runOnFinished = _runOnFinished
  }

  /**
   * Creates a [Timer] with the given [duration] and [runnables]. The [runnables] will be sorted by
   * their time and run in that order.
   *
   * @param duration the duration of the timer
   * @param _runnables the [TimeMarkedRunnable]s to run
   */
  constructor(
      duration: Float,
      _runnables: Array<TimeMarkedRunnable>
  ) : this(duration, false, _runnables)

  /**
   * Creates a [Timer] with the given [duration], [setToEnd], and [runnables]. The [runnables] will
   * be sorted by their time and run in that order.
   *
   * @param duration the duration of the timer
   * @param setToEnd if the timer should be set to the end
   * @param _runnables the [TimeMarkedRunnable]s to run
   */
  constructor(
      duration: Float,
      setToEnd: Boolean,
      _runnables: Array<TimeMarkedRunnable>
  ) : this(duration) {
    setRunnables(_runnables)
    time = if (setToEnd) duration else 0f
  }

  override fun update(delta: Float) {
    val finishedBefore = isFinished()

    time = min(duration, time + delta)

    while (!runnableQueue.isEmpty && runnableQueue.first().time <= time) {
      val runnable = runnableQueue.removeFirst()
      if (runnable.time <= time) break
      runnable.run()
    }

    justFinished = !finishedBefore && isFinished()
    if (justFinished) runOnFinished?.run()
  }

  override fun reset() {
    time = 0f
    justFinished = false

    runnableQueue.clear()
    val temp = Array(runnables)
    temp.sort()
    temp.forEach { runnableQueue.addLast(it) }
  }

  /**
   * Returns the ratio of the timer. The ratio is the [time] divided by the [duration]. If the
   * [duration] is 0, then the ratio will be 0.
   *
   * @return the ratio of the timer
   */
  fun getRatio() = if (duration > 0f) min(time / duration, 1f) else 0f

  /**
   * Returns if the timer is at the beginning. The timer is at the beginning if the [time] is 0.
   * This will return true if the timer is reset.
   *
   * @return if the timer is at the beginning
   */
  fun isAtBeginning() = time == 0f

  /**
   * Returns if the timer is finished. The timer is finished if the [time] is equal to the
   * [duration].
   *
   * @return if the timer is finished
   */
  fun isFinished() = time >= duration

  /**
   * Returns if the timer just finished. The timer just finished if the [time] was not equal to the
   * [duration] and is now equal to the [duration].
   *
   * @return if the timer just finished
   */
  fun isJustFinished() = justFinished

  /**
   * Sets the [runnables] to the given [_runnables]. The [runnables] will be sorted by their time
   * and run in that order.
   *
   * @param _runnables the [TimeMarkedRunnable]s to run
   * @return this [Timer] for chaining
   */
  fun setRunnables(_runnables: Array<TimeMarkedRunnable>): Timer {
    runnables.clear()
    runnables.addAll(_runnables)

    runnableQueue.clear()
    val temp = Array(_runnables)
    temp.sort()
    temp.forEach { runnableQueue.addLast(it) }

    return this
  }

  /** Clears the [runnables]. */
  fun clearRunnables(): Timer {
    runnables.clear()
    runnableQueue.clear()
    return this
  }

  /**
   * Sets the [time] to the [duration]. This will set [justFinished] to true if the [time] was not
   * equal to the [duration] and is now equal to the [duration].
   */
  fun setToEnd(): Timer {
    val oldTime = time
    time = duration
    justFinished = oldTime != time
    return this
  }
}
