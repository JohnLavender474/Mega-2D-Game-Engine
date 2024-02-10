package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
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
    override var hidden: Boolean = false
) : ISprite, Sprite() {

    constructor(
        texture: Texture,
        priority: DrawingPriority = DrawingPriority(DrawingSection.PLAYGROUND, 0),
        hidden: Boolean = false
    ) : this(priority, hidden) {
        setTexture(texture)
    }

    constructor(
        textureRegion: TextureRegion,
        priority: DrawingPriority = DrawingPriority(DrawingSection.PLAYGROUND, 0),
        hidden: Boolean = false
    ) : this(priority, hidden) {
        setRegion(textureRegion)
    }

    constructor(
        textureRegion: TextureRegion,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        priority: DrawingPriority = DrawingPriority(DrawingSection.PLAYGROUND, 0),
        hidden: Boolean = false
    ) : this(priority, hidden) {
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
        priority: DrawingPriority = DrawingPriority(DrawingSection.PLAYGROUND, 0),
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

    override fun setFlip(flipX: Boolean, flipY: Boolean) {
        super<Sprite>.setFlip(flipX, flipY)
    }
}
