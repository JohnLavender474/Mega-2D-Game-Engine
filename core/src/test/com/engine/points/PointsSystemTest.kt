package com.engine.points

import com.engine.GameEngine
import com.engine.common.objects.props
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class PointsSystemTest :
    DescribeSpec({
      describe("PointsSystem") {
        lateinit var pointsSystem: PointsSystem

        lateinit var entity1: IGameEntity
        lateinit var entity2: IGameEntity

        lateinit var points1: Points
        lateinit var points2: Points

        lateinit var pointsComponent1: PointsComponent
        lateinit var pointsComponent2: PointsComponent

        beforeEach {
          pointsSystem = PointsSystem()

          entity1 = GameEntity()
          entity2 = GameEntity()

          points1 = Points(0, 100, 50)
          points2 = Points(10, 90, 30)

          pointsComponent1 =
              PointsComponent(
                  "points1" to
                      PointsHandle(
                          points1,
                          listener = { newPoints -> newPoints shouldBe points1 },
                          onReset = { handle -> handle.points.setToMax() }))
          pointsComponent2 =
              PointsComponent(
                  "points2" to
                      PointsHandle(
                          points2,
                          listener = { newPoints -> newPoints shouldBe points2 },
                          onReset = { handle -> handle.points.setToMin() }))

          entity1.addComponent(pointsComponent1)
          entity2.addComponent(pointsComponent2)

          pointsSystem.addAll(entity1, entity2)
        }

        it("should call the listener for each entity's points component") {
          checkAll(Arb.int(0, 100)) { newValue ->
            points1.set(newValue)
            points2.set(newValue)
            pointsSystem.update(0.1f)
          }
        }

        it("should reset the points component") {
          val engine = GameEngine(pointsSystem)
          engine.spawn(entity1, props())
          engine.spawn(entity2, props())

          points1.set(50)
          points2.set(50)
          engine.update(0.1f)

          points1.current shouldBe 50
          points2.current shouldBe 50

          engine.reset()

          points1.current shouldBe 100
          points2.current shouldBe 10
        }
      }
    })
