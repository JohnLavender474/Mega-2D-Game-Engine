package com.engine.behaviors

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A [GameSystem] that processes [BehaviorsComponent]s. Each [AbstractBehavior] where its key returns true for
 * [BehaviorsComponent.isBehaviorAllowed] is updated.
 */
class BehaviorsSystem : GameSystem(BehaviorsComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach { entity ->
            val behaviorsComponent = entity.getComponent(BehaviorsComponent::class)!!
            behaviorsComponent.behaviors.forEach { entry ->
                val key = entry.key
                if (behaviorsComponent.isBehaviorAllowed(key)) {
                    val behavior = entry.value
                    behavior.update(delta)
                }
            }
        }
    }
}
