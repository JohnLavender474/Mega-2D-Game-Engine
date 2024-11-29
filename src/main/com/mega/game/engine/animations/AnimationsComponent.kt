package com.mega.game.engine.animations

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.drawables.sprites.GameSprite
import com.mega.game.engine.entities.contracts.ISpritesEntity
import java.util.*

class AnimationsComponent() : IGameComponent {

    companion object {
        const val TAG = "AnimationsComponent"
        const val DEFAULT_KEY = "default_key"
    }

    internal val sprites = ObjectMap<Any, GameSprite>()
    internal val animators = OrderedMap<Any, IAnimator>()

    constructor(entity: ISpritesEntity, animator: IAnimator): this() {
        val sprite = entity.defaultSprite
        putAnimator(sprite, animator)
    }

    /**
     * TODO: this constructor is here only to support compatibility with the old version of the engine. Any usages of
     *   this constructor should be removed and replaced.
     */
    constructor(animators: Array<GamePair<() -> GameSprite, IAnimator>>): this() {
        animators.forEach {
            val sprite = it.first.invoke()
            val animator = it.second
            val key = UUID.randomUUID().toString()
            putAnimator(key, sprite, animator)
        }
    }

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

class AnimationsComponentBuilder(var spritesEntity: ISpritesEntity? = null) {

    private val component = AnimationsComponent()
    private val sprites = component.sprites
    private val animators = component.animators

    private var currentKey: Any = AnimationsComponent.DEFAULT_KEY

    fun key(key: Any, fetchSpriteFromEntity: Boolean = true): AnimationsComponentBuilder {
        currentKey = key
        if (fetchSpriteFromEntity) spritesEntity?.let {
            val sprite = it.sprites[key]
            sprite(sprite)
        }
        return this
    }

    fun sprite(sprite: GameSprite): AnimationsComponentBuilder {
        sprites.put(currentKey, sprite)
        return this
    }

    fun sprite(key: Any, sprite: GameSprite): AnimationsComponentBuilder {
        key(key, false)
        sprite(sprite)
        return this
    }

    fun animator(animator: IAnimator): AnimationsComponentBuilder {
        if (!sprites.containsKey(currentKey)) {
            val sprite = spritesEntity?.let { it.sprites[currentKey] }
            if (sprite == null) throw IllegalStateException(
                "No sprite set or contained in sprites entity for key=$currentKey"
            )
            sprite(sprite)
        }
        animators.put(currentKey, animator)
        return this
    }

    fun put(key: Any, sprite: GameSprite, animator: IAnimator): AnimationsComponentBuilder {
        key(key, false)
        sprite(sprite)
        animator(animator)
        return this
    }

    fun build() = component
}
