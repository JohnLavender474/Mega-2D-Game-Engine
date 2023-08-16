package com.engine

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import kotlin.reflect.KClass

abstract class System(componentMask: Collection<KClass<out Component>>) : Updatable, Resettable {

    protected val entities: ArrayList<Entity> = ArrayList()
    protected val entitiesToAdd: ArrayList<Entity> = ArrayList()
    protected val componentMask: HashSet<KClass<out Component>> = HashSet(componentMask)

    protected var on = true
    var updating = true
        private set

    private var purgeEntities = false

    protected abstract fun processEntity(e: Entity, delta: Float)

    protected fun preProcess(delta: Float) {}

    protected fun postProcess(delta: Float) {}

    fun purgeAllEntities() = if (updating) purgeEntities = true else entities.clear()

    fun addEntityIfQualifies(e: Entity) = if (isQualified(e)) entitiesToAdd.add(e) else false

    fun isQualified(e: Entity) = componentMask.all { e.hasComponent(it) }

    override fun update(delta: Float) {
        updating = true
        entities.addAll(entitiesToAdd)
        entitiesToAdd.clear()
        entities.filter { !it.dead && isQualified(it) }
        if (on) {
            preProcess(delta)
            entities.forEach { processEntity(it, delta) }
            postProcess(delta)
        }
        updating = false
    }

    override fun reset() {
        entities.clear()
        entitiesToAdd.clear()
    }

    override fun toString(): String = this::class.simpleName ?: ""

}