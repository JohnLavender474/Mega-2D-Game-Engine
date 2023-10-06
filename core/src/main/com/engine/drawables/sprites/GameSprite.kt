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

  override fun setRegion(region: TextureRegion?) {
    super<Sprite>.setRegion(region)
  }

  override fun setTexture(texture: Texture?) {
    super<Sprite>.setTexture(texture)
  }

  override fun translate(x: Float, y: Float) {
    super<Sprite>.translate(x, y)
  }

  override fun setSize(width: Float, height: Float) {
    super<Sprite>.setSize(width, height)
  }

  override fun setWidth(width: Float) {
    super<Sprite>.setSize(width, height)
  }

  override fun setHeight(height: Float) {
    super<Sprite>.setSize(width, height)
  }

  override fun compareTo(other: IGameSprite) = priority.compareTo(other.priority)
}
