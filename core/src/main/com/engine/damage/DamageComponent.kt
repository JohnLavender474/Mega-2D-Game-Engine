package com.engine.damage

import com.engine.components.IGameComponent
import java.util.*

/**
 * A [DamageComponent] is a [IGameComponent] that contains a [Damageable] and a list of [Damager]s.
 * The [Damageable] is the object that can take damage from the [Damager]s. The [Damager]s are the
 * objects that can deal damage to the [Damageable]. The list of [Damager]s should be cleared at the
 * end of each update cycle.
 *
 * @param damageable the [Damageable] that can take damage from the [Damager]s
 * @property damagers the [Damager]s that can deal damage to the [Damageable
 */
class DamageComponent(internal var damageable: Damageable) : IGameComponent {

  var damagers: MutableList<Damager> = LinkedList()
}
