package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * A sprite that can be drawn. This class extends the [Sprite] class and implements the [ISprite]
 * interface.
 *
 * @param hidden whether this sprite is hidden
 */
class GameSprite(
    var hidden: Boolean = false,
) : ISprite, Sprite() {

  constructor(texture: Texture, hidden: Boolean = false) : this(hidden) {
    setTexture(texture)
  }

  constructor(textureRegion: TextureRegion, hidden: Boolean = false) : this(hidden) {
    setRegion(textureRegion)
  }

  constructor(
      textureRegion: TextureRegion,
      x: Float,
      y: Float,
      width: Float,
      height: Float,
      hidden: Boolean = false
  ) : this(hidden) {
    setRegion(textureRegion)
    setPosition(x, y)
    setSize(width, height)
  }

  constructor(
      texture: Texture,
      x: Float,
      y: Float,
      width: Float,
      height: Float,
      hidden: Boolean = false
  ) : this(hidden) {
    setTexture(texture)
    setPosition(x, y)
    setSize(width, height)
  }

  override fun draw(batch: Batch) {
    if (!hidden && texture != null) {
      super<Sprite>.draw(batch)
    }
  }

  override fun setRegion(_region: TextureRegion) {
    super<Sprite>.setRegion(_region)
  }

  override fun setTexture(_texture: Texture) {
    super<Sprite>.setTexture(_texture)
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

  override fun setFlip(x: Boolean, y: Boolean) {
    super<Sprite>.setFlip(x, y)
  }
}
