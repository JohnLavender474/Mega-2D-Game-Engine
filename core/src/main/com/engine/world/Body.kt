package com.engine.world

import com.engine.common.interfaces.Resettable
import com.engine.common.interfaces.Updatable
import com.engine.common.shapes.GameRectangle
import kotlin.reflect.KClass
import kotlin.reflect.cast

class Body(
    width: Float,
    height: Float,
    var bodyType: BodyType,
    var physicsData: PhysicsData = PhysicsData(),
    var fixtures: ArrayList<Fixture> = ArrayList(),
    var userData: HashMap<String, Any?> = HashMap(),
    var preProcess: Updatable? = null,
    var postProcess: Updatable? = null
) : GameRectangle(0f, 0f, width, height), Resettable {

  internal var previousBounds = GameRectangle()

  fun getPreviousBounds() = GameRectangle(previousBounds)

  fun isBodyType(bodyType: BodyType) = this.bodyType == bodyType

  fun <T : Any> getUserData(key: String, c: KClass<T>) = c.cast(userData[key])

  fun setUserData(key: String, data: Any?) = userData.put(key, data)

  override fun reset() {
    physicsData.reset()
    fixtures.forEach { f ->
      val p = getCenterPoint().add(f.offsetFromBodyCenter)
      f.shape.setCenter(p)
    }
  }
}
