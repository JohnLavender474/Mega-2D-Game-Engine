package com.mega.game.engine.entities

import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.GameEngine
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.Initializable
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.components.IComponentBucket
import com.mega.game.engine.components.IGameComponent
import kotlin.reflect.KClass

/**
 * The interface for entities. The [components] property contains a map for the entity's properties. The [state]
 * property is used by the [GameEngine] class to disclose the current state of the entity.
 */
interface IGameEntity : IComponentBucket, IPropertizable, Initializable {

    val components: OrderedMap<KClass<out IGameComponent>, IGameComponent>
    var initialized: Boolean
    var spawned: Boolean

    /**
     * Whether this [GameEntity] can be spawned. If this is false, then the entity will not be spawned by the engine.
     *
     * @return rrue if this [GameEntity] can be spawned, otherwise false
     */
    fun canSpawn(spawnProps: Properties): Boolean

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