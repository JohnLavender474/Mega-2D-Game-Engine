package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.common.shapes.GameShape2D
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * A fixture for a [Body]. A fixture is a shape that is attached to a body. It can be used to detect
 * collisions with other fixtures.
 *
 * @param shape The shape of this fixture.
 * @param fixtureType The type of this fixture. Used to determine how this fixture interacts with
 *   other fixtures.
 * @param active Whether this fixture is active. If not active, this fixture will not be used to
 *   detect collisions.
 * @param attachedToBody Whether this fixture is attached to a body. If not attached to a body, this
 *   fixture will not move with the body it is attached to.
 * @param offsetFromBodyCenter The offset of this fixture from the center of the body it is attached
 *   to. Used to position this fixture relative to the body it is attached to.
 * @param userData The user data of this fixture. Can be used to store any data associated with this
 *   fixture.
 */
class Fixture(
    var shape: GameShape2D,
    var fixtureType: String,
    var active: Boolean = true,
    var attachedToBody: Boolean = true,
    var offsetFromBodyCenter: Vector2 = Vector2(),
    var userData: HashMap<String, Any?> = HashMap()
) {

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
   * Returns if this fixture's shape overlaps the given shape.
   *
   * @return Whether this fixture's shape overlaps the given shape.
   * @see GameShape2D.overlaps
   */
  fun overlaps(other: GameShape2D) = shape.overlaps(other)

  /**
   * Returns if this fixture overlaps the given fixture. Specifically, this method returns if this
   * fixture's shape overlaps the given fixture's shape.
   *
   * @param other The fixture to check if this fixture overlaps.
   * @see overlaps
   */
  fun overlaps(other: Fixture) = overlaps(other.shape)
}
