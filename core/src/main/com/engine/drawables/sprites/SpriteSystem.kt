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
open class SpriteSystem(private val spritesCollectionSupplier: () -> MutableCollection<ISprite>) :
    GameSystem(SpriteComponent::class) {

  /**
   * Creates a [SpriteSystem] where the provided [MutableCollection] is used to store the sprites to
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
      val spriteComponent = entity.getComponent(SpriteComponent::class)
      spriteComponent?.update(delta)
      spriteComponent?.sprites?.values()?.forEach { sprite -> sprites.add(sprite) }
    }
  }
}
