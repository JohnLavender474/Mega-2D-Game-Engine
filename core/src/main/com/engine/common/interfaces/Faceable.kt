package com.engine.common.interfaces

import com.engine.common.enums.Facing

/**
 * An interface for objects that can face left or right.
 *
 * @see Facing
 */
interface Faceable {

  /** The [Facing] of this [Faceable]. */
  var facing: Facing
}
