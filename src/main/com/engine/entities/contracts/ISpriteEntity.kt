package com.engine.entities.contracts

import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.sprites.ISprite
import com.engine.drawables.sprites.SpritesComponent
import com.engine.entities.IGameEntity

/** An entity containing sprites. */
interface ISpriteEntity : IGameEntity {

    /**
     * The map of sprites in the entity.
     */
    val sprites: OrderedMap<String, ISprite>
        get() = getSpritesComponent().sprites

    /**
     * Fetches the first sprite in the entity. This is useful for entities that only have one sprite.
     */
    val firstSprite: ISprite?
        get() = if (sprites.isEmpty) null else sprites.first().value

    /**
     * Gets the [SpritesComponent] from the entity.
     *
     * @return The [SpritesComponent] from the entity.
     */
    fun getSpritesComponent() = getComponent(SpritesComponent::class)!!

    /**
     * Puts an update function for a sprite.
     *
     * @param key The key of the sprite.
     * @param updateFunction The update function for the sprite.
     */
    fun putUpdateFunction(key: String, updateFunction: (Float, ISprite) -> Unit) {
        getSpritesComponent().putUpdateFunction(key, updateFunction)
    }
}
