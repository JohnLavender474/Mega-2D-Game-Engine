package com.engine.world

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.GameShape2DSupplier
import kotlin.reflect.KClass
import kotlin.reflect.cast

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
 * @param physicsData the [PhysicsData] of the body
 * @param fixtures the [ArrayList] of [Fixture]s of the body
 * @param userData the [HashMap] of user data of the body
 * @param preProcess the [Updatable] to run before the body is processed
 * @param postProcess the [Updatable] to run after the body is processed
 * @see GameRectangle
 * @see BodyType
 * @see PhysicsData
 * @see Fixture
 * @see Updatable
 * @see WorldSystem
 * @see Resettable
 */
class Body(
    var bodyType: BodyType,
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f,
    var physicsData: PhysicsData = PhysicsData(),
    var fixtures: Array<Fixture> = Array(),
    var userData: ObjectMap<String, Any?> = ObjectMap(),
    var preProcess: Updatable? = null,
    var postProcess: Updatable? = null
) : GameRectangle(x, y, width, height), GameShape2DSupplier, Resettable {

  /**
   * Creates a [Body] with the given [BodyType], [PhysicsData], [ArrayList] of [Fixture]s, and
   * [HashMap] of user data.
   *
   * @param bodyType the [BodyType] of the body
   * @param physicsData the [PhysicsData] of the body
   * @param fixtures the [ArrayList] of [Fixture]s of the body
   * @param userData the [HashMap] of user data of the body
   * @param preProcess the [Updatable] to run before the body is processed
   * @param postProcess the [Updatable] to run after the body is processed
   */
  constructor(
      bodyType: BodyType,
      physicsData: PhysicsData,
      fixtures: Array<Fixture> = Array(),
      userData: ObjectMap<String, Any?> = ObjectMap(),
      preProcess: Updatable? = null,
      postProcess: Updatable? = null
  ) : this(bodyType, 0f, 0f, 0f, 0f, physicsData, fixtures, userData, preProcess, postProcess)

  // the bounds of this body before the current world update cycle
  internal var previousBounds = GameRectangle()

  /**
   * Returns a copy of the previous bounds of this body. The previous bounds are the bounds of this
   * body before the current update cycle of the [WorldSystem] and is set from within the
   * [WorldSystem].
   *
   * @return the previous bounds of this body
   */
  fun getPreviousBounds() = GameRectangle(previousBounds)

  /**
   * Returns if the body has the given [BodyType].
   *
   * @param bodyType the [BodyType] to check
   * @return if the body is the given [BodyType]
   */
  fun isBodyType(bodyType: BodyType) = this.bodyType == bodyType

  /**
   * Returns the user data associated with the given key.
   *
   * @param key the key of the user data
   * @return the user data associated with the given key
   */
  fun getUserData(key: String) = userData[key]

  /**
   * Returns the user data associated with the given key.
   *
   * @param key the key of the user data
   * @param c the class of the user data
   * @return the user data associated with the given key
   */
  fun <T : Any> getUserData(key: String, c: KClass<T>) = c.cast(userData[key])

  /**
   * Sets the user data associated with the given key to the given data.
   *
   * @param key the key of the user data
   * @param data the data to set the user data to
   * @return the value associated with the given key
   */
  fun setUserData(key: String, data: Any?) = userData.put(key, data)

  /**
   * Resets the body to its default state by resetting its [PhysicsData] and resetting the positions
   * of its [Fixture]s to their default positions (offset from the center of the body).
   *
   * @see Resettable.reset
   */
  override fun reset() {
    previousBounds.set(this)
    physicsData.reset()
    fixtures.forEach { f ->
      val p = getCenterPoint().add(f.offsetFromBodyCenter)
      f.shape.setCenter(p)
    }
  }

  override fun getGameShape2D() = this

  override fun equals(other: Any?) = this === other

  override fun hashCode() = System.identityHashCode(this)

  override fun toString() =
      "Body(first=$x second=$y width=$width height=$height hashCode=${hashCode()} bodyType=$bodyType, " +
          "physicsData=$physicsData, fixtures=$fixtures, userData=$userData, 9preProcess=$preProcess, " +
          "postProcess=$postProcess, previousBounds=$previousBounds)"
}
