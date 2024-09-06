package com.mega.game.engine.entities

import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.Initializable
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.components.IComponentBucket
import com.mega.game.engine.components.IGameComponent
import com.mega.game.engine.GameEngine
import kotlin.reflect.KClass

/**
 * The interface for entities. The [components] property contains a map for the entity's properties. The [state]
 * property contains field defining the state of this entity. In almost all cases, the fields of the [state] object
 * should not be modified from outside of the [GameEngine] class. The [state] field is modified by the [GameEngine]
 * to reflect the state of the entity. Although the fields of [state] are visible and modifiable, it is recommended
 * to treat the fields as read-only except in certain circumstances. Also, in almost all cases, the following methods
 * should not be called from outside of the [GameEngine] class:
 * - [init]
 * - [onSpawn]
 * - [onDestroy]
 *
 * The [init] method is used to initialize an entity. This is called when the initialized field of [state] is false.
 * The [onSpawn] and [onDestroy] methods are called when the entity is spawned and destroyed by the game engine
 * respectively.
 */
interface IGameEntity : IComponentBucket, IPropertizable, Initializable {

    val components: OrderedMap<KClass<out IGameComponent>, IGameComponent>
    val state: GameEntityState

    /**
     * Whether this [GameEntity] can be spawned. If this is false, then the entity will not be spawned by the engine.
     *
     * @return rrue if this [GameEntity] can be spawned, otherwise false
     */
    fun canSpawn(): Boolean

    /**
     * Called when this entity is spawned by the game engine.
     *
     * @param spawnProps The [Properties] to spawn this [GameEntity] with
     */
    fun onSpawn(spawnProps: Properties)

    /**
     * Called when this entity is destroyed by the game engine.
     */
    fun onDestroy()
}