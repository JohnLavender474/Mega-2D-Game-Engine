package com.engine.entities.contracts

import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.sprites.ISprite
import com.engine.drawables.sprites.SpriteComponent
import com.engine.entities.IGameEntity

interface ISpriteEntity : IGameEntity {

  val sprites: OrderedMap<String, ISprite>
    get() = getComponent(SpriteComponent::class)!!.sprites

  val firstSprite: ISprite
    get() = sprites.first().value
}
