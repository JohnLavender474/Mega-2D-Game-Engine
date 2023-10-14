package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Array
import com.engine.common.objects.ImmutableCollection
import com.engine.drawables.IDrawable
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the sprites to be rendered. The array is NOT cleared on each
 * update, so it is up to the developer to clear the array before the update.
 *
 * @param sprites the array to hold the sprites to be rendered
 */
class SpriteSystem(private val sprites: Array<IDrawable<Batch>>) :
    GameSystem(SpriteComponent::class) {

  override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
    if (!on) return

    // collect the sprites
    entities.forEach { entity ->
      val spriteComponent = entity.getComponent(SpriteComponent::class)
      spriteComponent?.update(delta)
      spriteComponent?.sprites?.values()?.forEach { sprite -> sprites.add(sprite) }
    }
  }
}
