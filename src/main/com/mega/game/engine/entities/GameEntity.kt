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
 *
 * The [initialized] and [spawned] are set and changed by the [GameEngine] class, and should be treated as read-only
 * variables in most cases.
 *
 * The `initialized` property determines if [init] should be called when the entity is spawned. The game engine will
 * call [init] and set `initialized` to true if `initialized` is false.
 *
 * The `spawned` property will be set to true when the entity is spawned by the game engine, and will be set to false
 * when the entity is destroyed by the game engine.
 *
 * @see [IGameEntity]
 * @see [GameEngine]
 */
abstract class GameEntity(
    val engine: GameEngine,
    override val components: OrderedMap<KClass<out IGameComponent>, IGameComponent> = OrderedMap(),
    override val properties: Properties = Properties()
) : IGameEntity {

    companion object {
        const val TAG = "GameEntity"
    }

    /**
     * Boolean value for whether this entity has been initialized. This field is set to true in the [GameEngine] class
     * the first time an entity is spawned in the game engine. Therefore, this value in most cases should be treated as
     * a read-only value.
     *
     * @see [GameEngine.spawn]
     */
    override var initialized = false

    /**
     * Boolean value for whether this entity is currently spawned. This field is set to true in the [GameEngine] class
     * each time an entity is spawned, and set to false each time an entity is destroyed.
     *
     * @see [GameEngine.spawn]
     * @see [GameEngine.destroy]
     */
    override var spawned = false

    /**
     * Default implementation of [init] is a no-op. In the child class, this method should be used to perform
     * initialization, e.g. instantiation of components. This method is called when the entity is initialized in the
     * game engine. Therefore, in most cases, this method should never be called from outside the game engine.
     *
     * @see [GameEngine.spawn]
     */
    override fun init() {}

    /**
     * Shorthand for `engine.spawn(this)`. Spawns this entity in the [engine]. Returns the result of [GameEngine.spawn].
     * Do not confuse this method with [onSpawn]. This method is merely a convenience method; meanwhile [onSpawn] is
     * called when the entity is spawned within the game engine.
     *
     * This method is final. Logic to perform when an entity is spawned should be contained in the [onSpawn] method.
     *
     * @return the result of [GameEngine.spawn]
     * @see [GameEngine.spawn]
     */
    fun spawn(spawnProps: Properties) = engine.spawn(this, spawnProps)

    /**
     * Shorthand for `engine.destroy(this)`. Destroys this entity in the [engine]. Returns the result of
     * [GameEngine.destroy].
     *
     * This method is final. Logic to perform when an entity is destroyed should be contained in the [onDestroy] method.
     *
     * @return the result of [GameEngine.destroy]
     * @see [GameEngine.destroy]
     */
    fun destroy() = engine.destroy(this)

    /**
     * Determines if the entity can be spawned. If this is false, this entity will not be spawned when passed through
     * the [GameEngine.spawn] method. Also, the entity will not be initialized by the game engine if it has not already
     * been initialized.
     *
     * Default return value is always true.
     *
     * @see [GameEngine.spawn]
     */
    override fun canSpawn() = true

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
}
