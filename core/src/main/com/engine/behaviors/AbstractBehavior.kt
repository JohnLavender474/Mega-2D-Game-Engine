package com.engine.behaviors

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An abstract class that represents a behavior. */
abstract class AbstractBehavior : Updatable, Resettable {

    private var runningNow = false
    private var forceQuit = false

    /**
     * Returns whether this [AbstractBehavior] is active.
     *
     * @return Whether this [AbstractBehavior] is active.
     */
    fun isActive() = runningNow

    /**
     * Evaluates this [AbstractBehavior] and returns whether it should be active.
     *
     * @param delta The time in seconds since the last frame.
     * @return Whether this [AbstractBehavior] should be active.
     */
    protected abstract fun evaluate(delta: Float): Boolean

    /** Initializes this [AbstractBehavior]. This is called when this [AbstractBehavior] becomes active. */
    protected abstract fun init()

    /**
     * Performs the action of this [AbstractBehavior].
     *
     * @param delta The time in seconds since the last frame.
     */
    protected abstract fun act(delta: Float)

    /** Ends this [AbstractBehavior]. This is called when this [AbstractBehavior] becomes inactive. */
    protected abstract fun end()

    /**
     * Forces this [AbstractBehavior] to quit. This will force this [AbstractBehavior] to disregard the result of its
     * [AbstractBehavior.evaluate] method for one update cycle and end this [AbstractBehavior].
     */
    fun forceQuit() {
        forceQuit = true
    }

    override fun update(delta: Float) {
        val runningPrior = runningNow
        runningNow = if (forceQuit) false else evaluate(delta)
        forceQuit = false

        if (runningNow && !runningPrior) init()
        if (runningNow) act(delta)
        if (!runningNow && runningPrior) end()
    }

    override fun reset() {
        runningNow = false
    }
}
