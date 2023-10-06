package com.engine.drawables.sprites

import com.badlogic.gdx.utils.OrderedMap
import com.engine.components.IGameComponent

/**
 * A component that can be used to store sprites.
 *
 * @param sprites The sprites.
 */
class SpriteComponent(var sprites: OrderedMap<String, IGameSprite>) : IGameComponent {

  /**
   * Creates a [SpriteComponent] with the given [sprites].
   *
   * @param _sprites The sprites to add to this [SpriteComponent].
   */
  constructor(
      vararg _sprites: Pair<String, IGameSprite>
  ) : this(
      OrderedMap<String, IGameSprite>().apply { _sprites.forEach { put(it.first, it.second) } })
}
