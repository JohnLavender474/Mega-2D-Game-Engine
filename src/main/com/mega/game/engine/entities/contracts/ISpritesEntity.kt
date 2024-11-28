package com.mega.game.engine.entities.contracts

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.interfaces.UpdateFunction
import com.mega.game.engine.drawables.sprites.GameSprite
import com.mega.game.engine.drawables.sprites.SpritesComponent
import com.mega.game.engine.entities.IGameEntity

interface ISpritesEntity : IGameEntity {

    val spritesComponent: SpritesComponent
        get() {
            val key = SpritesComponent::class
            return getComponent(key)!!
        }
    val sprites: ObjectMap<String, GameSprite>
        get() = this.spritesComponent.sprites
    val firstSprite: GameSprite
        get() = this.sprites.iterator().next().value

    fun putUpdateFunction(key: String, updateFunction: UpdateFunction<GameSprite>) {
        this.spritesComponent.putUpdateFunction(key, updateFunction)
    }
}
