package com.mega.game.engine.common.objects

import com.badlogic.gdx.math.MathUtils
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable


class SmoothOscillationTimer(
    private var duration: Float,
    private var start: Float = 0f,
    private var end: Float = 1f
) : Updatable, Resettable {

    private var elapsedTime = 0f
    private val halfDuration = duration / 2

    override fun update(delta: Float) {
        elapsedTime = (elapsedTime + delta) % duration
    }

    override fun reset() {
        elapsedTime = 0f
    }


    fun getValue() =
        if (elapsedTime <= halfDuration)
            MathUtils.lerp(start, end, elapsedTime / halfDuration)
        else
            MathUtils.lerp(end, start, (elapsedTime - halfDuration) / halfDuration)

}