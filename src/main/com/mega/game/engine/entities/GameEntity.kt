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
     * Default implementation of [init] is a no-op. In the child class, this method should be used to perform
     * initialization, e.g. instantiation of components.
     */
    override fun init() {}

    /**
     * Shorthand for `engine.spawn(this)`. Spawns this entity in the [engine]. Returns the result of [GameEngine.spawn].
     * Do not confuse this method with [onSpawn]. This method is merely a convenience method; meanwhile [onSpawn] is
     * called when the entity is spawned within the game engine.
     *
     * @return the result of [GameEngine.spawn]
     */
    fun spawn(spawnProps: Properties) = engine.spawn(this, spawnProps)

    /**
     * Shorthand for `engine.destroy(this)`. Destroys this entity in the [engine]. Returns the result of
     * [GameEngine.destroy].
     *
     * @return the result of [GameEngine.destroy]
     */
    fun destroy() = engine.destroy(this)

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
