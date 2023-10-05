package com.engine.cullables

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent

/**
 * A component that holds a list of [ICullable]s.
 *
 * @param cullables The list of [ICullable]s.
 */
class CullablesComponent(val cullables: Array<ICullable> = Array()) : IGameComponent {

  override fun reset() = cullables.forEach { it.reset() }
}
