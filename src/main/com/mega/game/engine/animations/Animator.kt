package com.mega.game.engine.animations

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.animations.Animator.Companion.DEFAULT_KEY
import com.mega.game.engine.common.extensions.objectMapOf
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.drawables.sprites.GameSprite


class Animator(
    val keySupplier: () -> String?,
    val animations: ObjectMap<String, IAnimation>,
    var updateScalar: Float = 1f,
    var onChangeKey: ((String?, String?) -> Unit)? = null
) : IAnimator {

    companion object {
        const val TAG = "Animator"
        const val DEFAULT_KEY = "Default"
    }

    
    val currentAnimation: IAnimation?
        get() = if (currentKey != null) animations[currentKey] else null

    
    var currentKey: String? = null
        private set

    
    constructor(animation: IAnimation) : this({ DEFAULT_KEY }, objectMapOf(DEFAULT_KEY pairTo animation))

    override fun animate(sprite: GameSprite, delta: Float) {
        val nextKey = keySupplier()
        if (currentKey != nextKey) {
            onChangeKey?.invoke(currentKey, nextKey)
            currentAnimation?.reset()
        }
        currentKey = nextKey
        currentAnimation?.let {
            it.update(delta * updateScalar)
            it.getCurrentRegion()?.let { region -> sprite.setRegion(region) }
        }
    }

    
    override fun reset() {
        currentKey = null
        updateScalar = 1f
        animations.values().forEach { it.reset() }
    }
}
