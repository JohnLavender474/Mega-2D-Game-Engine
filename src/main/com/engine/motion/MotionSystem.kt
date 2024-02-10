package com.engine.motion

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/** A system that updates the [MotionComponent]s of [GameEntity]s. */
class MotionSystem : GameSystem(MotionComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach {
            it.getComponent(MotionComponent::class)?.let { motionComponent ->
                motionComponent.definitions.values().forEach { definition ->
                    definition.motion.update(delta)
                    val value = definition.motion.getMotionValue()
                    if (value != null) definition.function(value, delta)
                }
            }
        }
    }
}
