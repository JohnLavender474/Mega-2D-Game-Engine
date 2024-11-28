package com.mega.game.engine.world.body

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.enums.Direction
import com.mega.game.engine.common.extensions.exp
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.common.objects.Properties
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.common.shapes.GameRectangle
import kotlin.math.abs

class Body(
    override var type: BodyType,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    var physics: PhysicsData = PhysicsData(),
    var fixtures: Array<GamePair<Any, IFixture>> = Array(),
    var preProcess: OrderedMap<Any, () -> Unit> = OrderedMap(),
    var postProcess: OrderedMap<Any, () -> Unit> = OrderedMap(),
    var onReset: (() -> Unit)? = null,
    override var direction: Direction = Direction.UP,
    override var properties: Properties = Properties(),
) : IBody, IPropertizable {

    companion object {
        const val TAG = "Body"
    }

    override var x: Float
        get() = bounds.getX()
        set(value) {
            bounds.setX(value)
        }
    override var y: Float
        get() = bounds.getY()
        set(value) {
            bounds.setY(value)
        }
    override var width: Float
        get() = bounds.getWidth()
        set(value) {
            bounds.setWidth(value)
        }
    override var height: Float
        get() = bounds.getHeight()
        set(value) {
            bounds.setHeight(value)
        }

    private val bounds = GameRectangle(x, y, width, height)
    private val tempVec1 = Vector2()

    override fun getBounds(out: GameRectangle): GameRectangle {
        out.set(bounds)
        val center = bounds.getCenter(tempVec1)
        out.setRotation(direction.rotation, center.x, center.y, out)
        return out
    }

    override fun addFixture(fixture: IFixture) = fixtures.add(fixture.getType() pairTo fixture)

    override fun getFixtures(out: Array<IFixture>): Array<IFixture> {
        fixtures.forEach { out.add(it.second) }
        return out
    }

    override fun forEachFixture(action: (IFixture) -> Unit) = fixtures.forEach {
        action.invoke(it.second)
    }

    override fun preProcess() = preProcess.values().forEach { it.invoke() }

    override fun process(delta: Float) {
        if (physics.applyFrictionX && physics.frictionOnSelf.x > 0f)
            physics.velocity.x *= exp(-physics.frictionOnSelf.x * delta)
        if (physics.applyFrictionY && physics.frictionOnSelf.y > 0f)
            physics.velocity.y *= exp(-physics.frictionOnSelf.y * delta)
        physics.frictionOnSelf.set(physics.defaultFrictionOnSelf)

        if (physics.gravityOn) physics.velocity.add(physics.gravity)

        physics.velocity.x =
            physics.velocity.x.coerceIn(-abs(physics.velocityClamp.x), abs(physics.velocityClamp.x))
        physics.velocity.y =
            physics.velocity.y.coerceIn(-abs(physics.velocityClamp.y), abs(physics.velocityClamp.y))

        x += physics.velocity.x * delta
        y += physics.velocity.y * delta
    }

    override fun postProcess() = postProcess.values().forEach { it.invoke() }

    override fun reset() {
        physics.reset()
        onReset?.invoke()
    }

    override fun equals(other: Any?) = this === other

    override fun hashCode() = System.identityHashCode(this)
}
