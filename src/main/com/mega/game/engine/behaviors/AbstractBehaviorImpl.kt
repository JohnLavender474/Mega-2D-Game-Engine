package com.mega.game.engine.behaviors


abstract class AbstractBehaviorImpl : IBehavior {

    private var runningNow = false
    private var forceQuit = false


    final override fun isActive() = runningNow


    final override fun reset() {
        if (runningNow) {
            end()
            runningNow = false
        }
    }


    final override fun update(delta: Float) {
        val runningPrior = runningNow
        runningNow = if (forceQuit) false else evaluate(delta)

        if (runningNow && !runningPrior) init()
        if (runningNow) act(delta)
        if (!runningNow && runningPrior) end()

        forceQuit = false
    }
}
