package com.engine.points

import com.engine.common.interfaces.Resettable

/**
 * Points are a value between the minimum stat and the maximum stat.
 *
 * @param min The minimum value.
 * @param max The maximum value.
 * @param current The current value.
 * @param onReset The function to call when [reset] is called.
 */
class Points(var min: Int, var max: Int, current: Int, var onReset: (() -> Unit)? = null) :
    Resettable {

  var current = current
    private set

  override fun reset() {
    onReset?.invoke()
  }

  /**
   * Sets the current stat. If the current stat is less than the minimum stat, the current stat will
   * be set to the minimum stat. If the current stat is greater than the maximum stat, the current
   * stat will be set to the maximum stat.
   *
   * @param points The current pointsHandles.
   */
  fun set(points: Int) {
    current = points
    if (current < min) {
      current = min
    }
    if (current > max) {
      current = max
    }
  }

  /**
   * Translates the current stat by the specified delta. If the current stat is less than the
   * minimum stat, the current stat will be set to the minimum stat. If the current stat is greater
   * than the maximum stat, the current stat will be set to the maximum stat.
   *
   * @param delta The delta.
   */
  fun translate(delta: Int) {
    set(current + delta)
  }

  /** Sets the current stat to the max stat. */
  fun setToMax() = set(max)

  /** Sets the current stat to the min stat. */
  fun setToMin() = set(min)
}
