package com.mega.game.engine.animations

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.drawables.sprites.GameSprite

class AnimationsComponent : IGameComponent {

    companion object {
        const val TAG = "AnimationsComponent"
        const val DEFAULT_KEY = "default_key"
    }

    internal val sprites = ObjectMap<Any, GameSprite>()
    internal val animators = OrderedMap<Any, IAnimator>()

    fun putAnimator(sprite: GameSprite, animator: IAnimator) = putAnimator(DEFAULT_KEY, sprite, animator)

    fun putAnimator(key: Any, sprite: GameSprite, animator: IAnimator) {
        sprites.put(key, sprite)
        animators.put(key, animator)
    }

    fun containsAnimator(key: Any) = animators.containsKey(key)

    fun removeAnimator(key: Any) {
        sprites.remove(key)
        animators.remove(key)
    }

    fun getAnimator(key: Any) = animators[key]

    override fun reset() = animators.values().forEach { it.reset() }
}

class AnimationsComponentBuilder {

    private val component = AnimationsComponent()
    private val sprites = component.sprites
    private val animators = component.animators

    private var currentKey: Any = AnimationsComponent.DEFAULT_KEY

    fun key(key: Any): AnimationsComponentBuilder {
        currentKey = key
        return this
    }

    fun sprite(sprite: GameSprite): AnimationsComponentBuilder {
        sprites.put(currentKey, sprite)
        return this
    }

    fun sprite(key: Any, sprite: GameSprite): AnimationsComponentBuilder {
        key(key)
        sprite(sprite)
        return this
    }

    fun animator(animator: IAnimator): AnimationsComponentBuilder {
        animators.put(currentKey, animator)
        return this
    }

    fun put(key: Any, sprite: GameSprite, animator: IAnimator): AnimationsComponentBuilder {
        key(key)
        sprite(sprite)
        animator(animator)
        return this
    }

    fun build() = component
}
