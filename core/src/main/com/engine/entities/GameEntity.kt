package com.engine.entities

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedSet
import com.engine.IGame2D
import com.engine.common.objects.Properties
import com.engine.components.IGameComponent
import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Standard implementation for [IGameEntity].
 *
 * @param game The [IGame2D] this [GameEntity] belongs to.
 */
open class GameEntity(override val game: IGame2D) : IGameEntity {

  val componentMap = ObjectMap<KClass<out IGameComponent>, IGameComponent>()

  override val runnablesOnSpawn = OrderedSet<Runnable>()
  override val runnablesOnDestroy = OrderedSet<Runnable>()
  override val properties = Properties()
  override var dead = false

  // if this is false, then the next call to [spawn] will call [init] and set this to true
  var initialized = false
    protected set

  /**
   * Destroys the entity. This method should be called when the entity is no longer needed. This
   * method will call [IGameComponent.reset] on all the entity's components. Also, the super method
   * will be called which calls all the runnables in [runnablesOnDestroy]. And lastly, [dead] is set
   * to true.
   */
  override fun onDestroy() {
    super.onDestroy()
    getComponents().forEach { it.reset() }
    runnablesOnDestroy.forEach { it.run() }
    dead = true
  }

  /**
   * Spawns the entity with the given [spawnProps]. If the entity has not been initialized, it will
   * be initialized. If the entity has already been initialized, it will not be initialized again.
   * If this method is overridden in the child implementation, then the child implementation should
   * call super.spawn(spawnProps) to ensure that the entity is initialized properly. Lastly, [dead]
   * is set to false.
   *
   * @param spawnProps The properties to spawn the entity with.
   */
  override fun spawn(spawnProps: Properties) {
    properties.putAll(spawnProps)
    if (!initialized) {
      init()
      initialized = true
    }
    runnablesOnSpawn.forEach { it.run() }
    dead = false
  }

  override fun addComponent(c: IGameComponent) {
    componentMap.put(c::class, c)
  }

  override fun <C : IGameComponent> getComponent(c: KClass<C>) =
      if (hasComponent(c)) c.cast(componentMap[c]) else null

  override fun getComponents(): Iterable<IGameComponent> = componentMap.values()

  override fun hasComponent(c: KClass<out IGameComponent>) = componentMap.containsKey(c)

  override fun removeComponent(c: KClass<out IGameComponent>): IGameComponent =
      componentMap.remove(c)

  override fun clearComponents() = componentMap.clear()

  override fun print() = "${this::class.simpleName}"
}
