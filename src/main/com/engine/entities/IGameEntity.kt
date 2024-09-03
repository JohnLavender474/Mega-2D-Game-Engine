package com.engine.entities

import com.engine.common.interfaces.IPrintable
import com.engine.common.interfaces.IPropertizable
import com.engine.common.interfaces.Initializable
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass

/**
 * An [IGameEntity] is a container for [IGameComponent]s. It is the base class for all entities in
 * the game. It is also a [IPropertizable] object, so it can have properties added to it.
 *
 * @see IPropertizable
 * @see IGameComponent
 */
interface IGameEntity : IPropertizable, Initializable, IPrintable {

    /**
     * True if this [GameEntity] is dead, otherwise false.
     */
    var dead: Boolean

    /**
     * Whether this [GameEntity] can be spawned.
     *
     * @return True if this [GameEntity] can be spawned, otherwise false.
     */
    fun canSpawn(): Boolean

    /**
     * Initializes this [GameEntity] with the given [Properties].
     *
     * @param spawnProps The [HashMap] of data to initialize this [GameEntity] with.
     */
    fun spawn(spawnProps: Properties)

    /**
     * Kills this [IGameEntity].
     *
     * @param props The [Properties] to pass to the entity when it is killed.
     */
    fun kill(props: Properties? = null)

    /** Logic to run when this entity is destroyed. */
    fun onDestroy()

    /**
     * Adds a [IGameComponent] to this [IGameEntity].
     *
     * @param c The [IGameComponent] to add.
     */
    fun addComponent(c: IGameComponent)

    /**
     * Adds a collection of [IGameComponent]s to this [IGameEntity].
     *
     * @param components The collection of [IGameComponent]s to add.
     * @see addComponent
     */
    fun addComponents(components: Iterable<IGameComponent>) = components.forEach { addComponent(it) }

    /**
     * Gets a [IGameComponent] from this [IGameEntity].
     *
     * @param c The [KClass] of the [IGameComponent] to get.
     * @return The [IGameComponent] if it exists, otherwise null.
     */
    fun <C : IGameComponent> getComponent(c: KClass<C>): C?

    /**
     * Gets a [IGameComponent] from this [IGameEntity].
     *
     * @param c The [Class] of the [IGameComponent] to get.
     * @return The [IGameComponent] if it exists, otherwise null.
     */
    fun <C : IGameComponent> getComponent(c: Class<C>) = getComponent(c.kotlin)

    /**
     * Gets all the [IGameComponent]s from this [IGameEntity].
     *
     * @return All the [IGameComponent]s from this [IGameEntity].
     */
    fun getComponents(): Iterable<IGameComponent>

    /**
     * Checks if this [GameEntity] has a [IGameComponent] with the given [KClass].
     *
     * @param c The [KClass] of the [IGameComponent] to check for.
     * @return True if this [GameEntity] has a [IGameComponent] with the given [KClass], otherwise
     *   false.
     */
    fun hasComponent(c: KClass<out IGameComponent>): Boolean

    /**
     * Checks if this [GameEntity] has a [IGameComponent] with the given [Class].
     *
     * @param c The [Class] of the [IGameComponent] to check for.
     * @return True if this [GameEntity] has a [IGameComponent] with the given [Class], otherwise
     *  false.
     */
    fun hasComponent(c: Class<out IGameComponent>) = hasComponent(c.kotlin)

    /**
     * Removes a [IGameComponent] from this [IGameEntity].
     *
     * @param c The [KClass] of the [IGameComponent] to remove.
     * @return The [IGameComponent] that was removed.
     */
    fun removeComponent(c: KClass<out IGameComponent>): IGameComponent?

    /**
     * Removes a [IGameComponent] from this [IGameEntity].
     *
     * @param c The [Class] of the [IGameComponent] to remove.
     * @return The [IGameComponent] that was removed.
     */
    fun removeComponent(c: Class<out IGameComponent>) = removeComponent(c.kotlin)

    /** Clears all the [IGameComponent]s from this [IGameEntity]. */
    fun clearComponents()

    /**
     * Gets the tag of this [IGameEntity]. The tag is a string that can be used to identify this
     * [IGameEntity], especially when debugging.
     *
     * @return The tag of this [IGameEntity].
     */
    fun getTag(): String
}
