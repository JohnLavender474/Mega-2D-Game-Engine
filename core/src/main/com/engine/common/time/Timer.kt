package com.engine.common.time

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import java.util.*
import kotlin.math.min

class Timer(val duration: Float = 0f) : Updatable, Resettable {

  internal var runnables: ArrayList<TimeMarkedRunnable> = ArrayList()
  internal var runnableQueue: Queue<TimeMarkedRunnable> = PriorityQueue()

  var time = 0f
    private set
  var justFinished = false
    private set

  var runOnFinished: Runnable? = null

  constructor(timer: Timer) : this(timer.duration) {
    runnables.clear()
    runnables.addAll(timer.runnables)
    runnableQueue.clear()
    runnableQueue.addAll(timer.runnableQueue)
    time = timer.time
    justFinished = timer.justFinished
    runOnFinished = timer.runOnFinished
  }

  constructor(duration: Float, _runOnFinished: Runnable) : this(duration) {
    runOnFinished = _runOnFinished
  }

  constructor(
      duration: Float,
      _runnables: Collection<TimeMarkedRunnable>
  ) : this(duration, false, _runnables)

  constructor(
      duration: Float,
      setToEnd: Boolean,
      _runnables: Collection<TimeMarkedRunnable>
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
    while (runnableQueue.isNotEmpty() && runnableQueue.peek().time <= time) {
      val runnable = runnableQueue.poll()
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
    runnableQueue.addAll(runnables)
  }

  fun getRatio() = if (duration > 0f) min(time / duration, 1f) else 0f

  fun isAtBeginning() = time == 0f

  fun isFinished() = time >= duration

  fun isJustFinished() = justFinished

  fun setRunnables(_runnables: Collection<TimeMarkedRunnable>): Timer {
    runnables.clear()
    runnables.addAll(_runnables)
    runnableQueue.clear()
    runnableQueue.addAll(runnables)
    return this
  }

  fun clearRunnables() {
    runnables.clear()
    runnableQueue.clear()
  }

}
