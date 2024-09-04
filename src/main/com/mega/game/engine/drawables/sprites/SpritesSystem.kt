package com.mega.game.engine.drawables.sprites

import com.mega.game.engine.common.objects.ImmutableCollection
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.systems.GameSystem
import java.util.function.Consumer

/**
 * A system that can be used to gather the sprites to be rendered. This system will collect the sprites
 * from the entities that have a [SpritesComponent] and pass them to the provided [spritesCollector].
 *
 * @param spritesCollector the function that should collect the sprites to be drawn
 */
class SpritesSystem(protected val spritesCollector: (GameSprite) -> Unit) :
    GameSystem(SpritesComponent::class) {

    /**
     * Creates a [SpritesSystem] with the given [spritesCollector].
     *
     * @param spritesCollector the function that should collect the sprites to be drawn
     */
    constructor(spritesCollector: Consumer<GameSprite>) : this(spritesCollector::accept)

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