package com.engine.behaviors

/**
 * An abstract implementation of [IBehavior]. The implementations for [isActive], [update] and [reset] are final.
 */
abstract class AbstractBehaviorImpl : IBehavior {

    private var runningNow = false
    private var forceQuit = false

    /**
     * Returns whether this [AbstractBehaviorImpl] is active.
     *
     * @return Whether this [AbstractBehaviorImpl] is active.
     */
    final override fun isActive() = runningNow

    /**
     * Resets this [AbstractBehaviorImpl] by forcing it to quit if it is running. This will call [end] immediately and
     * force this [AbstractBehaviorImpl] to be reset for the next update cycle. If this behavior is not active, then
     * this method does nothing.
     */
    final override fun reset() {
        if (runningNow) {
            end()
            runningNow = false
        }
    }

    /**
     * Updates this [AbstractBehaviorImpl]. This method should be called once per frame. This method will evaluate this
     * [AbstractBehaviorImpl], initialize it if it is active, act if it is active, and end it if it is inactive.
     *
     * @param delta The time in seconds since the last frame.
     */
    final override fun update(delta: Float) {
        val runningPrior = runningNow
        runningNow = if (forceQuit) false else evaluate(delta)

        if (runningNow && !runningPrior) init()
        if (runningNow) act(delta)
        if (!runningNow && runningPrior) end()

        forceQuit = false
    }
}
