package com.engine.behaviors

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An abstract class that represents a behavior. */
abstract class IBehavior : Updatable, Resettable {

  private var runningNow = false

  /**
   * Returns whether this [IBehavior] is active.
   *
   * @return Whether this [IBehavior] is active.
   */
  fun isActive() = runningNow

  /**
   * Evaluates this [IBehavior] and returns whether it should be active.
   *
   * @param delta The time in seconds since the last frame.
   * @return Whether this [IBehavior] should be active.
   */
  protected abstract fun evaluate(delta: Float): Boolean

  /** Initializes this [IBehavior]. This is called when this [IBehavior] becomes active. */
  protected abstract fun init()

  /**
   * Performs the action of this [IBehavior].
   *
   * @param delta The time in seconds since the last frame.
   */
  protected abstract fun act(delta: Float)

  /** Ends this [IBehavior]. This is called when this [IBehavior] becomes inactive. */
  protected abstract fun end()

  override fun update(delta: Float) {
    val runningPrior = runningNow
    runningNow = evaluate(delta)
    if (runningNow && !runningPrior) {
      init()
    }
    if (runningNow) {
      act(delta)
    }
    if (!runningNow && runningPrior) {
      end()
    }
  }

  override fun reset() {
    runningNow = false
  }
}
