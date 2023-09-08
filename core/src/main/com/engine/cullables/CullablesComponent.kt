package com.engine.cullables

import com.engine.GameComponent

/**
 * A component that holds a list of [Cullable]s.
 *
 * @param cullables The list of [Cullable]s.
 */
class CullablesComponent(val cullables: MutableList<Cullable> = ArrayList()) : GameComponent {

  override fun reset() = cullables.forEach { it.reset() }
}
