package com.engine.common.time

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import java.util.*
import kotlin.math.min

class Timer : Updatable, Resettable {

    private val runnables: ArrayList<TimeMarkedRunnable> = ArrayList()
    private val runnableQueue: Queue<TimeMarkedRunnable> = PriorityQueue()

    private var time = 0f
    private var duration = 0f
    private var justFinished = false

    private var runOnFinished: Runnable? = null

    override fun update(delta: Float) {
        val finishedBefore = isFinished()
        time = min(duration, time + delta)
        while (runnableQueue.isNotEmpty() && runnableQueue.peek().time <= time) {
            val runnable = runnableQueue.poll()
            if (runnable.time <= time) {
                break
            }
            runnable.runnable.run()
        }
        justFinished = !finishedBefore && isFinished()
        if (justFinished) {
            runOnFinished?.run()
        }
    }

    override fun reset() {
        time = 0f
        runnableQueue.clear()
        runnableQueue.addAll(runnables)
    }

    fun getRatio() = if (duration > 0f) min(time / duration, 1f) else 0f

    fun isAtBeginning() = time == 0f

    fun isFinished() = time >= duration

    fun isJustFinished() = justFinished

    fun setToTimer(timer: Timer): Timer {
        runnables.clear()
        runnableQueue.clear()
        runnables.addAll(timer.runnables)
        runnableQueue.addAll(timer.runnableQueue)
        time = timer.time
        duration = timer.duration
        justFinished = timer.justFinished
        runOnFinished = timer.runOnFinished
        return this
    }

    fun setDuration(_duration: Float): Timer {
        duration = _duration
        return this
    }

    fun setRunnables(_runnables: Collection<TimeMarkedRunnable>): Timer {
        runnables.clear()
        runnables.addAll(_runnables)
        runnableQueue.clear()
        return this
    }

    fun setToEnd(): Timer {
        time = duration
        return this
    }
}