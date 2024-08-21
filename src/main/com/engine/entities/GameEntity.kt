package com.engine.entities

import com.badlogic.gdx.utils.OrderedMap
import com.badlogic.gdx.utils.OrderedSet
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Standard implementation for [IGameEntity].
 */
open class GameEntity : IGameEntity {

    companion object {
        const val TAG = "GameEntity"
    }

    val components = OrderedMap<KClass<out IGameComponent>, IGameComponent>()
    val runnablesOnSpawn = OrderedSet<Runnable>()
    val runnablesOnDestroy = OrderedSet<Runnable>()

    override val properties = Properties()
    override var dead = false

    /**
     * Whether this [GameEntity] has been initialized. If this is false, then [init] will be called
     * when [spawn] is called.
     */
    var initialized = false

    /**
     * Empty implementation. Override this method to add logic to run when this [GameEntity] is instantiated.
     */
    override fun init() {}

    /**
     * Spawns this [GameEntity] with the given [spawnProps]. If this [GameEntity] has not been initialized,
     * [init] will be called and [initialized] will be set to true within this method. All [runnablesOnSpawn]
     * will be run at the end.
     *
     * @param spawnProps The [Properties] to spawn this [GameEntity] with.
     */
    override fun spawn(spawnProps: Properties) {
        dead = false
        properties.putAll(spawnProps)
        if (!initialized) {
            init()
            initialized = true
        }
        runnablesOnSpawn.forEach { it.run() }
    }

    override fun kill(props: Properties?) {
        dead = true
        properties.clear()
    }

    override fun onDestroy() {
        runnablesOnDestroy.forEach { it.run() }
        getComponents().forEach { it.reset() }
    }

    override fun addComponent(c: IGameComponent) {
        components.put(c::class, c)
    }

    override fun <C : IGameComponent> getComponent(c: KClass<C>) =
        if (hasComponent(c)) c.cast(components[c]) else null

    override fun getComponents(): Iterable<IGameComponent> = components.values()

    override fun hasComponent(c: KClass<out IGameComponent>) = components.containsKey(c)

    override fun removeComponent(c: KClass<out IGameComponent>): IGameComponent? =
        components.remove(c)

    override fun clearComponents() = components.clear()

    override fun print() = "${this::class.simpleName}"

    override fun getTag() = TAG
}
