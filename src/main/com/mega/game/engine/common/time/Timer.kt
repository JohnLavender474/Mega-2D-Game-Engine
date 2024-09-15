package com.mega.game.engine.common.time

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable
import kotlin.math.min

/**
 * A timer that can be used to keep track of time and run [TimeMarkedRunnable]s at specific times.
 * The timer can be reset and will run the [TimeMarkedRunnable]s again.
 *
 * @param _duration the duration of the timer
 */
class Timer(_duration: Float) : Updatable, Resettable {

    companion object {
        const val DEFAULT_TIME = 1f
    }

    internal var runnables: Array<TimeMarkedRunnable> = Array()
    internal var runnableQueue = Queue<TimeMarkedRunnable>()

    var duration = _duration
        private set
    var time = 0f
        private set
    var justFinished = false
        private set

    var runOnFirstUpdate: (() -> Unit)? = null
    var runOnFinished: (() -> Unit)? = null

    private var firstUpdate = true

    /**
     * Creates a [Timer] with the default time of 1.
     */
    constructor() : this(DEFAULT_TIME)

    /**
     * Creates a [Timer] with the given [duration] and [runnables]. The [runnables] will be sorted by
     * their time and run in that order.
     *
     * @param duration the duration of the timer
     * @param _runnables the [TimeMarkedRunnable]s to run
     */
    constructor(
        duration: Float, vararg _runnables: TimeMarkedRunnable
    ) : this(duration, Array(_runnables))

    /**
     * Creates a [Timer] with the given [duration] and [runnables]. The [runnables] will be sorted by
     * their time and run in that order.
     *
     * @param duration the duration of the timer
     * @param _runnables the [TimeMarkedRunnable]s to run
     */
    constructor(
        duration: Float, _runnables: Array<TimeMarkedRunnable>
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
        duration: Float, setToEnd: Boolean, _runnables: Array<TimeMarkedRunnable>
    ) : this(duration) {
        setRunnables(_runnables)
        time = if (setToEnd) duration else 0f
    }

    override fun update(delta: Float) {
        if (firstUpdate) {
            runOnFirstUpdate?.invoke()
            firstUpdate = false
        }
        val finishedBefore = isFinished()
        time = min(duration, time + delta)
        while (!runnableQueue.isEmpty && runnableQueue.first().time <= time) runnableQueue.removeFirst().run()
        justFinished = !finishedBefore && isFinished()
        if (justFinished) runOnFinished?.invoke()
    }

    /**
     * Resets the timer. This will set the [time] to 0 and set [justFinished] to false. The [runnables]
     * will be sorted by their time and run in that order. This will also clear the [runnableQueue].
     * This will not change the [duration]. The runnable [runOnFirstUpdate] will be run on the next update.
     */
    override fun reset() {
        time = 0f
        justFinished = false
        firstUpdate = true
        runnableQueue.clear()
        val temp = Array(runnables)
        temp.sort()
        temp.forEach { runnableQueue.addLast(it) }
    }

    /**
     * Sets the duration and resets the timer.
     *
     * @param duration the duration to set the timer to
     */
    fun resetDuration(duration: Float) {
        this.duration = duration
        reset()
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
     * Sets the [time] to the [duration]. This will set [justFinished] to true if the [time] was not equal to the
     * [duration] and is now equal to the [duration] and also if [allowJustFinished] is true.
     *
     * @param allowJustFinished If this is true, then [isJustFinished] will return true until the next time [update]
     * is called. However, if this is false, then [isJustFinished] will return false.
     * @return this timer for chaining
     */
    fun setToEnd(allowJustFinished: Boolean = true): Timer {
        val oldTime = time
        time = duration
        justFinished = if (allowJustFinished) oldTime != time else false
        return this
    }
}
