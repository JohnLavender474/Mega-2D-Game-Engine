package com.engine.entities.contracts

import com.engine.common.interfaces.Positional
import com.engine.common.interfaces.Sizable
import com.engine.entities.IGameEntity
import com.engine.world.Body
import com.engine.world.BodyComponent

interface IBodyEntity : IGameEntity, Positional, Sizable {

  val body: Body
    get() = getComponent(BodyComponent::class)!!.body

  override fun getX() = body.x

  override fun getY() = body.y

  override fun getPosition() = body.getPosition()

  override fun setPosition(x: Float, y: Float) {
    body.setPosition(x, y)
  }

  override fun getWidth() = body.width

  override fun getHeight() = body.height

  override fun setSize(width: Float, height: Float) {
    body.setSize(width, height)
  }

  override fun setWidth(width: Float) {
    body.setWidth(width)
  }

  override fun setHeight(height: Float) {
    body.setHeight(height)
  }

  override fun translateSize(width: Float, height: Float) {
    body.setWidth(body.width + width)
    body.setHeight(body.height + height)
  }

  override fun translate(x: Float, y: Float) {
    body.translation(x, y)
  }
}
