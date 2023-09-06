package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/**
 * An interface for objects that have a motion value. The motion value is the value that is used to
 * move the object.
 */
interface Motion : Updatable, Resettable {

  /**
   * Gets the motion value of this object. The motion value is the value that is used to move the
   * object. The [Vector2] can either provide positional values, velocity values, or any other
   * values that can be used to move the object. It is up to the implementation to decide what
   * values are returned. This should be clear from the documentation of the implementation.
   *
   * @return the motion value of this object
   */
  fun getMotionValue(): Vector2?
}
