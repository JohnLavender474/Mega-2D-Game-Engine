package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import kotlin.reflect.KClass

abstract class GameSystem(componentMask: Collection<KClass<out Component>>) :
    Updatable, Resettable {

  private val entities: ArrayList<Entity> = ArrayList()
  private val entitiesToAdd: ArrayList<Entity> = ArrayList()
  private val componentMask: HashSet<KClass<out Component>> = HashSet(componentMask)

  var on = true
  var updating = true
    private set

  private var purgeEntities = false

  protected abstract fun process(on: Boolean, entities: ArrayList<Entity>, delta: Float)

  fun purgeAllEntities() = if (updating) purgeEntities = true else entities.clear()

  fun addEntityIfQualifies(e: Entity) = if (isQualified(e)) entitiesToAdd.add(e) else false

  fun isQualified(e: Entity) = componentMask.all { e.hasComponent(it) }

  override fun update(delta: Float) {
    updating = true
    entities.addAll(entitiesToAdd)
    entitiesToAdd.clear()
    entities.filter { !it.dead && isQualified(it) }
    process(on, entities, delta)
    updating = false
  }

  override fun reset() {
    entities.clear()
    entitiesToAdd.clear()
  }

  override fun toString(): String = this::class.simpleName ?: ""
}
