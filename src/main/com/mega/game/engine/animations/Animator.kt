package com.mega.game.engine.animations

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.extensions.objectMapOf
import com.mega.game.engine.drawables.sprites.GameSprite
import java.util.function.Supplier

/**
 * An animator that can be used to animate a sprite. The animator is created with a key supplier
 * that is used to determine which animation to play. The key supplier is called every update and
 * the returned key is used to determine which animation to play. The animator is also created with
 * a map of animations that are used to animate the sprite.
 *
 * @param keySupplier the key supplier that is used to determine which animation to play
 * @param animations the animations that are used to animate the sprite
 * @param updateScalar the scalar that is used to speed up or slow down the current animation; default is 1.0f
 * @see IAnimator
 */
class Animator(
    val keySupplier: () -> String?,
    val animations: ObjectMap<String, IAnimation>,
    var updateScalar: Float = 1f
) : IAnimator {

    companion object {
        const val TAG = "Animator"
        const val DEFAULT_KEY = "Default"
    }

    /**
     * The current animation that is being played. This value is determined by the current key. If
     * the current key is null, then the current animation is also null.
     */
    val currentAnimation: IAnimation?
        get() = if (currentKey != null) animations[currentKey] else null

    /**
     * The current key that is used to determine which animation to play. Private setter to prevent
     * external modification.
     */
    var currentKey: String? = null
        private set

    /**
     * Convenience constructor if only one animation is needed. Creates an animator with the specified
     * animation. The animator is created with a default key supplier that always returns
     * [DEFAULT_KEY]. Also, the [animations] map is created with the [DEFAULT_KEY] and the specified
     * animation.
     *
     * @param animation the animation to animate the sprite with
     */
    constructor(animation: IAnimation) : this({ DEFAULT_KEY }, objectMapOf(DEFAULT_KEY to animation))

    /**
     * Convenience constructor if using Java [Supplier] to supply the key.
     *
     * @param keySupplier the key supplier that is used to determine which animation to play
     * @param animations the animations that are used to animate the sprite
     */
    constructor(keySupplier: Supplier<String>, animations: ObjectMap<String, IAnimation>) : this(
        { keySupplier.get() },
        animations
    )

    override fun animate(sprite: GameSprite, delta: Float) {
        val nextKey = keySupplier()

        // if the key has changed, then reset the current animation before setting the key
        if (currentKey != nextKey) currentAnimation?.reset()

        currentKey = nextKey
        // after the key has been set to the new key, then also reset the new animation too
        if (currentKey != nextKey) currentAnimation?.reset()

        currentAnimation?.let {
            it.update(delta * updateScalar)
            it.getCurrentRegion()?.let { region -> sprite.setRegion(region) }
        }
    }

    /**
     * Resets the current key, all animations, and resets the update scalar to 1.0f.
     */
    override fun reset() {
        currentKey = null
        updateScalar = 1f
        animations.values().forEach { it.reset() }
    }
}
