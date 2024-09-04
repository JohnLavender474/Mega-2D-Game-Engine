package com.mega.game.engine.cullables

import com.mega.game.engine.GameEngine
import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.GameEntity
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/**
 * A [GameSystem] that culls [GameEntity]s with [CullablesComponent]s. Culled entities are marked as
 * dead. This means they will not be destroyed until the end of the [GameEngine]'s update cycle.
 *
 * @see CullablesComponent
 */
class CullablesSystem(private val engine: GameEngine) : GameSystem(CullablesComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return
        entities.forEach { entity ->
            if (!entity.gameEntityState.spawned) return
            val cullables = entity.getComponent(CullablesComponent::class)?.cullables?.values()
            for (cullable in cullables ?: return) if (cullable.shouldBeCulled(delta)) {
                engine.destroy(entity)
                break
            }
        }
    }
}
