package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedMap
import com.engine.common.GameLogger
import com.engine.common.enums.Direction
import com.engine.common.interfaces.IPropertizable
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle

/**
 * A [Body] is a [GameRectangle] representing the body of an object in the game world. It contains a
 * [BodyType] which determines how it interacts with other bodies. It also contains a [PhysicsData]
 * object which contains information about the body's physical properties. It also contains a list
 * of [Fixture]s which are used to determine collisions. It also contains a [HashMap] of user data
 * which can be used to store any data associated with the body. It also contains a [preProcess] and
 * [postProcess] [Updatable] which are used to perform any actions before or after the body is
 * processed by the [WorldSystem].
 *
 * @param bodyType the [BodyType] of the body
 * @param x the first position of the body
 * @param y the second position of the body
 * @param width the width of the body
 * @param height the height of the body
 * @param physics the [PhysicsData] of the body
 * @param fixtures the [OrderedMap] of [Fixture]s of the body
 * @param properties the [Properties] of the body
 * @param preProcess the [Updatable] to run before the body is processed
 * @param postProcess the [Updatable] to run after the body is processed
 * @param cardinalRotation the [Direction] of the body; this determines the rotation of the body
 *   returned by [rotatedBounds]. If the [cardinalRotation] is [Direction.UP], then the rotation is
 *   0f which means essentially that no rotation is performed. This field is used in determining
 *   body collisions in [WorldSystem] and [StandardCollisionHandler]. The default value is
 *   [Direction.UP]. If you do not wish to use the built-in rotation logic, then it is best to keep
 *   this field the default value.
 * @param originXCenter if true, the center x of the body will be used as the origin x instead of
 *   the value of [originX] when calculating [rotatedBounds]. The origin only affects the value of
 *   [rotatedBounds]. If your intention is to merely rotate the body relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @param originYCenter if true, the center y of the body will be used as the origin y instead of
 *   the value of [originY] when calculating [rotatedBounds]. The origin only affects the value of
 *   [rotatedBounds]. If your intention is to merely rotate the body relative to itself (i.e. rotate
 *   but not move), then you should set this to true. The default value is true. If you do not wish
 *   to use the built-in rotation logic, then it is best to keep this field the default value.
 * @see GameRectangle
 * @see BodyType
 * @see PhysicsData
 * @see Fixture
 * @see Updatable
 * @see WorldSystem
 * @see Resettable
 */
open class Body(
    var bodyType: BodyType,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    var physics: PhysicsData = PhysicsData(),
    var fixtures: Array<Pair<Any, Fixture>> = Array(),
    override var properties: Properties = Properties(),
    var preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var postProcess: OrderedMap<Any, Updatable> = OrderedMap(),
    var cardinalRotation: Direction = Direction.UP,
    var originXCenter: Boolean = true,
    var originYCenter: Boolean = true
) : GameRectangle(x, y, width, height), Resettable, IPropertizable {

  companion object {
    const val TAG = "Body"
  }

  /**
   * Creates a [Body] with the given [BodyType], [PhysicsData], [ArrayList] of [Fixture]s, and
   * [HashMap] of user data.
   *
   * @param bodyType the [BodyType] of the body
   * @param physicsData the [PhysicsData] of the body
   * @param fixtures the [OrderedMap] of [Fixture]s of the body
   * @param properties the [Properties] of the body
   * @param preProcess the [Updatable] to run before the body is processed
   * @param postProcess the [Updatable] to run after the body is processed
   */
  constructor(
      bodyType: BodyType,
      physicsData: PhysicsData,
      fixtures: Array<Pair<Any, Fixture>> = Array(),
      properties: Properties = Properties(),
      preProcess: OrderedMap<Any, Updatable> = OrderedMap(),
      postProcess: OrderedMap<Any, Updatable> = OrderedMap()
  ) : this(bodyType, 0f, 0f, 0f, 0f, physicsData, fixtures, properties, preProcess, postProcess)

  // the bounds of this body before the current world update cycle
  internal var previousBounds = GameRectangle()

  /**
   * The difference between the current center point and the previous center point. Set in each
   * update cycle of the [WorldSystem].
   */
  val positionDelta: Vector2
    get() = getCenterPoint().sub(previousBounds.getCenterPoint())

  /**
   * Returns the bounds of this body rotated in the given [Direction]. If the [cardinalRotation] is
   * [Direction.UP], then the rotation is 0f which means essentially that no rotation is performed.
   * If [originXCenter] is true, then the center x of the body will be used as the origin x instead
   * of the value of [originX]. If [originYCenter] is true, then the center y of the body will be
   * used as the origin y instead of the value of [originY]. The origin only affects the value of
   * [rotatedBounds]. The default value of [cardinalRotation] is [Direction.UP]. The default value
   * of [originXCenter] is true. The default value of [originYCenter] is true.
   */
  open val rotatedBounds: GameRectangle
    get() {
      val center = getCenter()
      val copy = GameRectangle(this)
      copy.originX = if (originXCenter) center.x else originX
      copy.originY = if (originYCenter) center.y else originY
      return copy.getCardinallyRotatedShape(cardinalRotation, false)
    }

  /**
   * Returns a copy of the previous bounds of this body. The previous bounds are the bounds of this
   * body before the current update cycle of the [WorldSystem] and is set from within the
   * [WorldSystem].
   *
   * @return the previous bounds of this body
   */
  open fun getPreviousBounds() = GameRectangle(previousBounds)

  /**
   * Returns if the body has the given [BodyType].
   *
   * @param bodyType the [BodyType] to check
   * @return if the body is the given [BodyType]
   */
  fun isBodyType(bodyType: BodyType) = this.bodyType == bodyType

  /**
   * Adds the given [Fixture] to this body. The fixture is mapped to the fixture's label.
   *
   * @param fixture the [Fixture] to add
   */
  open fun addFixture(fixture: Fixture): Body {
    fixtures.add(fixture.fixtureLabel to fixture)
    return this
  }

  /**
   * Runs the given function for each fixture entry in this body.
   *
   * @param function the function to run for each fixture entry
   */
  fun forEachFixture(function: (Any, Fixture) -> Unit) =
      fixtures.forEach { function(it.first, it.second) }

  /**
   * Resets the body to its default state by resetting its [PhysicsData] and resetting the positions
   * of its [Fixture]s to their default positions (offset from the center of the body).
   *
   * @see Resettable.reset
   */
  override fun reset() {
    previousBounds.set(this)
    physics.reset()
    fixtures.forEach { f ->
      val p = getCenterPoint().add(f.second.offsetFromBodyCenter)
      f.second.shape.setCenter(p)
    }
  }

  override fun copy(): Body {
    GameLogger.debug(TAG, "copy(): Creating copy of body: $this.")
    val body =
        Body(
            bodyType,
            x,
            y,
            width,
            height,
            physics.copy(),
            Array(),
            properties.copy(),
            preProcess,
            postProcess)
    fixtures.forEach { body.addFixture(it.second.copy()) }
    return body
  }

  override fun equals(other: Any?) = this === other

  override fun hashCode() = System.identityHashCode(this)
}
