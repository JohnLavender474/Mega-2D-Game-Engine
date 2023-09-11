package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.engine.common.shapes.isInCamera

/**
 * A sprite that can be drawn. This class extends the [Sprite] class and implements the
 * [ISprite] interface.
 *
 * @param textureRegion the texture region to use
 * @param priority the priority of this sprite
 * @param hidden whether this sprite is hidden
 */
class Sprite(
    textureRegion: TextureRegion,
    override var priority: Int,
    var hidden: Boolean = false,
) : ISprite, Sprite(textureRegion) {

  override fun isInCamera(camera: Camera) = boundingRectangle.isInCamera(camera)

  override fun draw(batch: Batch) {
    if (!hidden && texture != null) {
      super<Sprite>.draw(batch)
    }
  }

  override fun compareTo(other: ISprite) = priority.compareTo(other.priority)
}
