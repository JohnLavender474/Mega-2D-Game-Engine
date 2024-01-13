package com.engine.common.enums

/** Represents a cardinal direction. */
enum class Direction {
  UP,
  DOWN,
  LEFT,
  RIGHT;

  /**
   * Returns true if this direction is horizontal.
   *
   * @return True if this direction is horizontal.
   */
  fun isHorizontal() = this == LEFT || this == RIGHT

  /**
   * Returns true if this direction is vertical.
   *
   * @return True if this direction is vertical.
   */
  fun isVertical() = this == UP || this == DOWN
}
