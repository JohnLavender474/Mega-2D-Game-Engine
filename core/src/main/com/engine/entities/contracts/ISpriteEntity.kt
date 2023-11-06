package com.engine.entities.contracts

import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.sprites.ISprite
import com.engine.drawables.sprites.SpriteComponent
import com.engine.entities.IGameEntity

/** An entity containing sprites. */
interface ISpriteEntity : IGameEntity {

  val sprites: OrderedMap<String, ISprite>
    get() = getComponent(SpriteComponent::class)!!.sprites

  val firstSprite: ISprite?
    get() = if (sprites.isEmpty) null else sprites.first().value
}
