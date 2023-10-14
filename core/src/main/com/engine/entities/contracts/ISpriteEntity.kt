package com.engine.entities.contracts

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.OrderedMap
import com.engine.drawables.IDrawable
import com.engine.drawables.sprites.SpriteComponent
import com.engine.entities.IGameEntity

interface ISpriteEntity : IGameEntity {

  val sprites: OrderedMap<String, IDrawable<Batch>>
    get() = getComponent(SpriteComponent::class)!!.sprites

  val firstSprite: IDrawable<Batch>
    get() = sprites.first().value
}
