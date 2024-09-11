package com.mega.game.engine.cullables

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.GameEntity
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * Interface for entity cullers. Default implementation can be the following:
 * ```
 * class MyGameEntityCuller(val engine: GameEngine) {
 *
 *     override fun cull(entity: IGameEntity) {
 *         engine.destroy(entity)
 *     }
 * }
 * ```
 *
 * or, if it's guaranteed that every entity extends [GameEntity], then:
 * ```
 * class MyGameEntityCuller {
 *
 *     override fun cull(entity: IGameEntity) {
 *         (entity as GameEntity).destroy()
 *     }
 * }
 * ```
 */
interface GameEntityCuller {

    /**
     * Should cull the entity.
     *
     * @param entity The entity to cull
     */
    fun cull(entity: IGameEntity)
}

/**
 * A [GameSystem] that culls [GameEntity]s with [CullablesComponent]s. Culled entities are passed to the [culler]
 * property whose job it is to "cull" the entity. In most case, the lambda, should destroy the entity.
 *
 * @property culler The destroyer of entities
 * @see CullablesComponent
 */
class CullablesSystem(private val culler: GameEntityCuller) : GameSystem(CullablesComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return
        entities.forEach { entity ->
            val cullables = entity.getComponent(CullablesComponent::class)?.cullables?.values()
            for (cullable in cullables ?: return) if (cullable.shouldBeCulled(delta)) {
                culler.cull(entity)
                break
            }
        }
    }
}
