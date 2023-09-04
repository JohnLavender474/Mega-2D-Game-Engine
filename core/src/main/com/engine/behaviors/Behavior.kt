package com.engine.behaviors

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable

/** An abstract class that represents a behavior. */
abstract class Behavior : Updatable, Resettable {

  private var runningNow = false

  /**
   * Returns whether this [Behavior] is active.
   *
   * @return Whether this [Behavior] is active.
   */
  fun isActive() = runningNow

  /**
   * Evaluates this [Behavior] and returns whether it should be active.
   *
   * @param delta The time in seconds since the last frame.
   * @return Whether this [Behavior] should be active.
   */
  protected abstract fun evaluate(delta: Float): Boolean

  /** Initializes this [Behavior]. This is called when this [Behavior] becomes active. */
  protected abstract fun init()

  /**
   * Performs the action of this [Behavior].
   *
   * @param delta The time in seconds since the last frame.
   */
  protected abstract fun act(delta: Float)

  /** Ends this [Behavior]. This is called when this [Behavior] becomes inactive. */
  protected abstract fun end()

  /** Optional method to run when this [Behavior] is reset. */
  protected open fun onReset() {}

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

  final override fun reset() {
    runningNow = false
    onReset()
  }
}
