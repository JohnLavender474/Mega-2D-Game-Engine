package com.engine.points

import com.engine.SimpleMockEntity
import com.engine.common.extensions.gdxArrayOf
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class PointsSystemTest :
    DescribeSpec({
      describe("PointsSystem") {
        it("should call the pointsHandles listener for each entity's pointsHandles") {
          val pointsSystem = PointsSystem()

          val entity1 = SimpleMockEntity()
          val entity2 = SimpleMockEntity()

          val points1 = Points(0, 100, 50)
          val points2 = Points(10, 90, 30)

          val pointsComponent1 =
              PointsComponent(
                  gdxArrayOf(
                          PointsHandle(points1) { newPoints -> newPoints shouldBe points1 }))
          val pointsComponent2 =
              PointsComponent(
                  gdxArrayOf(
                          PointsHandle(points2) { newPoints -> newPoints shouldBe points2 }))

          entity1.addComponent(pointsComponent1)
          entity2.addComponent(pointsComponent2)

          pointsSystem.addAll(entity1, entity2)

          checkAll(Arb.int(0, 100)) { newValue ->
            points1.set(newValue)
            points2.set(newValue)
            pointsSystem.update(0.1f)
          }
        }
      }
    })
