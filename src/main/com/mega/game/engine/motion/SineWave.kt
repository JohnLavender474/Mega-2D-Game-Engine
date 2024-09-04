package com.mega.game.engine.motion

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

/**
 * A [SineWave] is an [IMotion] that moves in a sine wave pattern.
 *
 * @property speed the speed of the wave.
 * @property amplitude the amplitude of the wave.
 * @property frequency the frequency of the wave.
 */
class SineWave(var position: Vector2, var speed: Float, var amplitude: Float, var frequency: Float) : IMotion {

    private var elapsedTime = 0f

    override fun getMotionValue() = position

    override fun update(delta: Float) {
        elapsedTime += delta
        position.x += speed * delta
        position.y += amplitude * MathUtils.sin(frequency * elapsedTime)
    }

    override fun reset() {
        elapsedTime = 0f
    }
}