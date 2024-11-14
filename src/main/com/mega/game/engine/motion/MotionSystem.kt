package com.mega.game.engine.motion

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.GameEntity
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem

/** A system that updates the [MotionComponent]s of [GameEntity]s. */
class MotionSystem : GameSystem(MotionComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        entities.forEach {
            it.getComponent(MotionComponent::class)?.let { motionComponent ->
                motionComponent.definitions.values().forEach { definition ->
                    if (definition.doUpdate()) {
                        val motion = definition.motion
                        motion.update(delta)
                        val value = motion.getMotionValue()
                        if (value != null) definition.function(value, delta)
                    }
                }
            }
        }
    }
}
