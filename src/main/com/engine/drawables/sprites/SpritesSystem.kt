package com.engine.drawables.sprites

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the sprites to be rendered. This system will collect the sprites
 * from the entities that have a [SpritesComponent] and pass them to the provided [spritesCollector].
 *
 * @param spritesCollector the function that should collect the sprites to be drawn
 */
open class SpritesSystem(protected open val spritesCollector: (GameSprite) -> Unit) :
    GameSystem(SpritesComponent::class) {

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        // collect the sprites into the supplied collection
        entities.forEach { entity ->
            val spritesComponent = entity.getComponent(SpritesComponent::class)
            spritesComponent?.update(delta)
            spritesComponent?.sprites?.values()?.forEach { sprite -> spritesCollector.invoke(sprite) }
        }
    }
}
