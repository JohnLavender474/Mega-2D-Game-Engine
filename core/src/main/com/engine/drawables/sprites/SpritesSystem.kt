package com.engine.drawables.sprites

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the sprites to be rendered. The set is NOT cleared on each
 * update, so it is up to the developer to clear the set before the update.
 *
 * @param spritesCollectionSupplier the supplier that supplies the collection of sprites
 */
open class SpritesSystem(private val spritesCollectionSupplier: () -> MutableCollection<ISprite>) :
    GameSystem(SpritesComponent::class) {

  /**
   * Creates a [SpritesSystem] where the provided [MutableCollection] is used to store the sprites to
   * be rendered.
   *
   * @param sprites the collection to store the sprites to be rendered
   */
  constructor(sprites: MutableCollection<ISprite>) : this({ sprites })

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
