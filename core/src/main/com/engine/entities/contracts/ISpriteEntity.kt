package com.engine.entities.contracts

import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.sprites.IGameSprite
import com.engine.drawables.sprites.SpriteComponent
import com.engine.entities.IGameEntity

interface ISpriteEntity : IGameEntity {

  val sprites: OrderedMap<String, IGameSprite>
    get() = getComponent(SpriteComponent::class)!!.sprites

  val firstSprite: IGameSprite
    get() = sprites.first().value
}
