package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.engine.common.objects.ImmutableCollection
import com.engine.drawables.IDrawable
import com.engine.drawables.sorting.IComparableDrawable
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the sprites to be rendered. The collection passed to the
 * constructor is used to store the sprites to be rendered. The collection is supplied by the
 * provided supplier. The collection holds [IDrawable<Batch>] objects which means that any object
 * that implements [IDrawable<Batch>] can be added to the collection. It is guaranteed that all
 * elements collected through this system will implement [ISprite].
 *
 * @param spritesCollectionSupplier the supplier that supplies the collection to collect sprites into.
 */
open class SpritesSystem(protected val spritesCollectionSupplier: () -> MutableCollection<IDrawable<Batch>>) :
    GameSystem(SpritesComponent::class) {

    /**
     * Creates a [SpritesSystem] where the provided [MutableCollection] is used to store the sprites to
     * be rendered.
     *
     * @param sprites the collection to store the sprites to be rendered
     */
    constructor(sprites: MutableCollection<IDrawable<Batch>>) : this({ sprites })

    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        // collect the sprites into the supplied set
        val sprites = spritesCollectionSupplier()
        entities.forEach { entity ->
            val spritesComponent = entity.getComponent(SpritesComponent::class)
            spritesComponent?.update(delta)
            spritesComponent?.sprites?.values()?.forEach { sprite -> sprites.add(sprite) }
        }
    }
}
