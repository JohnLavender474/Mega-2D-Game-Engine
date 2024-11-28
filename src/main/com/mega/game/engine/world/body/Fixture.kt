package com.mega.game.engine.world.body

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.enums.Position
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.shapes.GameRectangle
import com.mega.game.engine.common.shapes.IGameShape2D
import com.mega.game.engine.common.shapes.IRotatableShape

class Fixture(
    var body: Body,
    type: Any,
    rawShape: IGameShape2D,
    active: Boolean = true,
    var attachedToBody: Boolean = true,
    var bodyAttachmentPosition: Position = Position.CENTER,
    var offsetFromBodyAttachment: Vector2 = Vector2(),
    override var properties: Properties = Properties(),
) : IFixture, ICopyable<Fixture> {

    companion object {
        const val TAG = "Fixture"
    }

    private var rawShape: IGameShape2D = rawShape
        set(value) {
            field = value
            adjustedShape = null
        }
    private var fixtureType = type
    private var isActive = active
    private var adjustedShape: IGameShape2D? = null

    private val reusableShapeProps = Properties()
    private val reusableGameRect = GameRectangle()
    private val out1 = Vector2()

    override fun setShape(shape: IGameShape2D) {
        rawShape = shape
    }

    override fun getShape(): IGameShape2D {
        if (adjustedShape == null) adjustedShape = rawShape.copy()

        val copy = adjustedShape!!

        reusableShapeProps.clear()
        rawShape.getProps(reusableShapeProps)
        copy.setWithProps(reusableShapeProps)

        if (!attachedToBody) return adjustedShape!!

        val attachmentPos = body.getBounds(reusableGameRect).getPositionPoint(bodyAttachmentPosition, out1)
        copy.setCenter(attachmentPos).translate(offsetFromBodyAttachment)

        if (copy is IRotatableShape) copy.setRotation(body.direction.rotation, attachmentPos.x, attachmentPos.y)

        return copy
    }

    override fun getType() = fixtureType

    override fun setType(type: Any) {
        fixtureType = type
    }

    override fun setActive(active: Boolean) {
        isActive = active
    }

    override fun isActive() = isActive

    override fun toString() =
        "Fixture(raw_shape=$rawShape, type=$fixtureType, active=$isActive, attachedToBody=$attachedToBody, " +
                "offsetFromBodyCenter=$offsetFromBodyAttachment, properties=$properties)"

    fun overlaps(other: IGameShape2D) = getShape().overlaps(other)

    fun overlaps(other: IFixture) = overlaps(other.getShape())

    override fun copy() = Fixture(
        body,
        getType(),
        rawShape.copy(),
        isActive,
        attachedToBody,
        bodyAttachmentPosition,
        offsetFromBodyAttachment.cpy(),
        Properties(properties)
    )
}
