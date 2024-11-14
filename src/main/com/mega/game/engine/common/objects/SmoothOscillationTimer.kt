package com.mega.game.engine.common.objects

import com.badlogic.gdx.math.MathUtils
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.interfaces.Updatable

/**
 * A [SmoothOscillationTimer] modulates a value between the min and max values using lerping.
 *
 * @property duration The total duration of one cycle.
 * @property start The start value.
 * @property end The end value.
 */
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

    /**
     * Gets the current value.
     * @return the current value.
     */
    fun getValue() =
        if (elapsedTime <= halfDuration)
            MathUtils.lerp(start, end, elapsedTime / halfDuration)
        else
            MathUtils.lerp(end, start, (elapsedTime - halfDuration) / halfDuration)

}