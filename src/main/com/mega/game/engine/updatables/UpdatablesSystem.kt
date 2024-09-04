package com.mega.game.engine.updatables

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.GameEntity
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/** The updatable system. Processes all the updatables of each [GameEntity]. */
class UpdatablesSystem : GameSystem(UpdatablesComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach { entity ->
            val updatablesComponent = entity.getComponent(UpdatablesComponent::class)
            updatablesComponent?.updatables?.forEach { it.update(delta) }
        }
    }
}
