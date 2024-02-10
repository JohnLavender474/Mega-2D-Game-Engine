package com.engine.behaviors

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A [GameSystem] that processes [BehaviorsComponent]s. Each [AbstractBehavior] is updated, and after each
 * one is updated, then [BehaviorsComponent.setActive] is called for each [AbstractBehavior]. The array of
 * behaviors in the [BehaviorsComponent] should be in the order wished to be updated. For example,
 * let's say you have two behaviors jump and swim, and you want to update jump first and not allow
 * the entity to swim if he/she is jumping. Then you would add jump to the array first and then
 * swim. This way jump will be updated first and then swim. If the jump behavior is active, then
 * [BehaviorsComponent.isBehaviorActive] will return true for jump, and this can be used to not
 * allow the entity to swim from within the swim behavior's logic.
 */
class BehaviorsSystem : GameSystem(BehaviorsComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach { entity ->
            entity.getComponent(BehaviorsComponent::class)?.let { b ->
                b.behaviors.forEach { e ->
                    val key = e.key
                    val behavior = e.value

                    behavior.update(delta)

                    val active = behavior.isActive()
                    b.setActive(key, active)
                }
            }
        }
    }
}
