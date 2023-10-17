package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.objects.Properties
import com.engine.drawables.sorting.DrawingPriority
import com.engine.drawables.sorting.DrawingSection

/**
 * A sprite that can be drawn. This class also extends the [Sprite].
 *
 * @param priority the priority of this sprite
 * @param hidden whether this sprite is hidden
 */
class GameSprite(
    override val priority: DrawingPriority = DrawingPriority(DrawingSection.PLAYGROUND, 0),
    var hidden: Boolean = false,
    override val properties: Properties = Properties()
) : ISprite, Sprite() {

  constructor(
      texture: Texture,
      priority: DrawingPriority,
      hidden: Boolean = false
  ) : this(priority, hidden) {
    setTexture(texture)
  }

  constructor(
      textureRegion: TextureRegion,
      priority: DrawingPriority,
      hidden: Boolean = false
  ) : this(priority, hidden) {
    setRegion(textureRegion)
  }

  constructor(
      textureRegion: TextureRegion,
      priority: DrawingPriority,
      x: Float,
      y: Float,
      width: Float,
      height: Float,
      hidden: Boolean = false
  ) : this(priority, hidden) {
    setRegion(textureRegion)
    setPosition(x, y)
    setSize(width, height)
  }

  constructor(
      texture: Texture,
      priority: DrawingPriority,
      x: Float,
      y: Float,
      width: Float,
      height: Float,
      hidden: Boolean = false
  ) : this(priority, hidden) {
    setTexture(texture)
    setPosition(x, y)
    setSize(width, height)
  }

  override fun draw(drawer: Batch) {
    if (!hidden && texture != null) {
      super<Sprite>.draw(drawer)
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

  override fun setFlip(flipX: Boolean, flipY: Boolean) {
    super<Sprite>.setFlip(flipX, flipY)
  }
}
