package com.engine.common.time

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import kotlin.math.min

class Timer(val duration: Float = 0f) : Updatable, Resettable {

  internal var runnables: Array<TimeMarkedRunnable> = Array()
  internal var runnableQueue = Queue<TimeMarkedRunnable>()

  var time = 0f
    private set

  var justFinished = false
    private set

  var runOnFinished: Runnable? = null

  constructor(duration: Float, _runOnFinished: Runnable) : this(duration) {
    runOnFinished = _runOnFinished
  }

  constructor(
      duration: Float,
      _runnables: Array<TimeMarkedRunnable>
  ) : this(duration, false, _runnables)

  constructor(
      duration: Float,
      setToEnd: Boolean,
      _runnables: Array<TimeMarkedRunnable>
  ) : this(duration) {
    setRunnables(_runnables)
    time =
        if (setToEnd) {
          duration
        } else {
          0f
        }
  }

  override fun update(delta: Float) {
    val finishedBefore = isFinished()
    time = min(duration, time + delta)
    while (!runnableQueue.isEmpty && runnableQueue.first().time <= time) {
      val runnable = runnableQueue.removeFirst()
      if (runnable.time <= time) {
        break
      }
      runnable.run()
    }
    justFinished = !finishedBefore && isFinished()
    if (justFinished) {
      runOnFinished?.run()
    }
  }

  override fun reset() {
    time = 0f
    justFinished = false

    runnableQueue.clear()
    val temp = Array(runnables)
    temp.sort()
    temp.forEach { runnableQueue.addLast(it) }
  }

  fun getRatio() = if (duration > 0f) min(time / duration, 1f) else 0f

  fun isAtBeginning() = time == 0f

  fun isFinished() = time >= duration

  fun isJustFinished() = justFinished

  fun setRunnables(_runnables: Array<TimeMarkedRunnable>): Timer {
    runnables.clear()
    runnables.addAll(_runnables)

    runnableQueue.clear()
    val temp = Array(_runnables)
    temp.sort()
    temp.forEach { runnableQueue.addLast(it) }

    return this
  }

  fun clearRunnables() {
    runnables.clear()
    runnableQueue.clear()
  }
}
