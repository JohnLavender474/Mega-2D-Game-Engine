package com.mega.game.engine.entities.contracts

import com.mega.game.engine.common.interfaces.IPositional
import com.mega.game.engine.common.interfaces.ISizable
import com.mega.game.engine.entities.IGameEntity
import com.mega.game.engine.world.body.Body
import com.mega.game.engine.world.body.BodyComponent

interface IBodyEntity : IGameEntity, IPositional, ISizable {

    val bodyComponent: BodyComponent
        get() {
            val key = BodyComponent::class
            return getComponent(key)!!
        }

    val body: Body
        get() = bodyComponent.body

    override fun getX(): Float {
        return this.body.getX()
    }

    override fun setX(x: Float) {
        this.body.setX(x)
    }

    override fun getY(): Float {
        return this.body.getY()
    }

    override fun setY(y: Float) {
        this.body.setY(y)
    }

    override fun setPosition(x: Float, y: Float) {
        this.body.setPosition(x, y)
    }

    override fun getWidth(): Float {
        return this.body.getWidth()
    }

    override fun setWidth(width: Float) {
        this.body.setWidth(width)
    }

    override fun getHeight(): Float {
        return this.body.getHeight()
    }

    override fun setHeight(height: Float) {
        this.body.setHeight(height)
    }

    override fun setSize(width: Float, height: Float) {
        this.body.setSize(width, height)
    }

    override fun translateSize(width: Float, height: Float) {
        this.body.setWidth(this.body.getWidth() + width)
        this.body.setHeight(this.body.getHeight() + height)
    }

    override fun translate(x: Float, y: Float) {
        this.body.translate(x, y)
    }
}