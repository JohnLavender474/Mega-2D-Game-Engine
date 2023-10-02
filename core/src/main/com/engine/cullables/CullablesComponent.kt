package com.engine.cullables

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent

/**
 * A component that holds a list of [Cullable]s.
 *
 * @param cullables The list of [Cullable]s.
 */
class CullablesComponent(val cullables: Array<Cullable> = Array()) : IGameComponent {

  override fun reset() = cullables.forEach { it.reset() }
}
