package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * A sprite that can be drawn. This class extends the [Sprite] class and implements the
 * [IGameSprite] interface.
 *
 * @param priority the priority of this sprite
 * @param hidden whether this sprite is hidden
 */
class GameSprite(
    override var priority: Int = 0,
    var hidden: Boolean = false,
) : IGameSprite, Sprite() {

  constructor(
      textureRegion: TextureRegion,
      priority: Int = 0,
      hidden: Boolean = false
  ) : this(priority, hidden) {
    setRegion(textureRegion)
  }

  constructor(
      textureRegion: TextureRegion,
      priority: Int = 0,
      hidden: Boolean = false,
      x: Float,
      y: Float
  ) : this(priority, hidden) {
    setRegion(textureRegion)
    setPosition(x, y)
  }

  override fun draw(batch: Batch) {
    if (!hidden && texture != null) {
      super<Sprite>.draw(batch)
    }
  }

  override fun setRegion(_region: TextureRegion?) {
    super<Sprite>.setRegion(_region)
  }

  override fun setTexture(_texture: Texture?) {
    super<Sprite>.setTexture(_texture)
  }

  override fun translate(_x: Float, _y: Float) {
    super<Sprite>.translate(_x, _y)
  }

  override fun setSize(_width: Float, _height: Float) {
    super<Sprite>.setSize(_width, _height)
  }

  override fun setWidth(_width: Float) {
    super<Sprite>.setSize(_width, height)
  }

  override fun setHeight(_height: Float) {
    super<Sprite>.setSize(width, _height)
  }

  override fun setFlip(_x: Boolean, _y: Boolean) {
    super<Sprite>.setFlip(_x, _y)
  }

  override fun compareTo(other: IGameSprite) = priority.compareTo(other.priority)
}
