package com.engine.cullables

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent
import com.engine.entities.IGameEntity

/**
 * A component that holds a list of [ICullable]s.
 *
 * @param cullables The list of [ICullable]s.
 */
class CullablesComponent(
    override val entity: IGameEntity,
    val cullables: Array<ICullable> = Array()
) : IGameComponent {

  override fun reset() = cullables.forEach { it.reset() }
}
