package com.mega.game.engine.entities

import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.GameEngine
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Default implementation of [IGameEntity] which takes a game engine, components map, and properties as parameters.
 * This implementation provides the open [spawn] and [destroy] methods which are shorthands for `engine.spawn(this)`
 * and `engine.destroy(this)` respectively. Having these methods also allows for custom logic to be implemented in the
 * spawn and destroy phases that would otherwise not be suitable for the [onSpawn] and [onDestroy] methods.
 */
abstract class GameEntity(
    val engine: GameEngine,
    override val components: OrderedMap<KClass<out IGameComponent>, IGameComponent> = OrderedMap(),
    override val state: GameEntityState = GameEntityState(),
    override val properties: Properties = Properties()
) : IGameEntity {

    companion object {
        const val TAG = "GameEntity"
    }

    /**
     * Shorthand for `engine.spawn(this)`. Spawns this entity in the [engine]. Returns the result of [GameEngine.spawn].
     * If this method is overriden, then the super method must be called or else the entity will not be spawned in the
     * game engine.
     *
     * @return the result of [GameEngine.spawn]
     */
    open fun spawn(spawnProps: Properties) = engine.spawn(this, spawnProps)

    /**
     * Shorthand for `engine.destroy(this)`. Destroys this entity in the [engine]. Returns the result of
     * [GameEngine.destroy]. If this method is overriden, then the super method must be called or else the
     * entity will not be destroyed in the game engine.
     *
     * @return the result of [GameEngine.destroy]
     */
    open fun destroy() = engine.destroy(this)

    override fun <C : IGameComponent> getComponent(key: KClass<C>): C? {
        val value = components[key] ?: return null
        return key.cast(value)
    }

    override fun addComponent(c: IGameComponent) {
        components.put(c::class, c)
    }

    override fun hasComponent(key: KClass<out IGameComponent>) = components.containsKey(key)

    override fun removeComponent(key: KClass<out IGameComponent>) {
        components.remove(key)
    }

    override fun clearComponents() = components.clear()

    override fun canSpawn() = true
}
