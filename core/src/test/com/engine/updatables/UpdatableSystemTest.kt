package com.engine.updatables

import com.engine.SimpleMockEntity
import com.engine.common.interfaces.Updatable
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.mockk.*

class UpdatableSystemTest :
    DescribeSpec({
      describe("UpdatableSystem") {
        it("should call the update method for each updatable in each entity") {
          val updatableSystem = UpdatableSystem()

          val entity1 = SimpleMockEntity()
          val entity2 = SimpleMockEntity()

          val updatable1 = mockk<Updatable> { every { update(any()) } just Runs }
          val updatable2 = mockk<Updatable> { every { update(any()) } just Runs }

          val updatableComponent1 = UpdatableComponent(arrayListOf(updatable1))
          val updatableComponent2 = UpdatableComponent(arrayListOf(updatable2))

          entity1.addComponent(updatableComponent1)
          entity2.addComponent(updatableComponent2)

          updatableSystem.addAll(entity1, entity2)

          checkAll(Arb.int(0, 100)) { delta ->
            updatableSystem.update(delta.toFloat())
            verify { updatable1.update(delta.toFloat()) }
            verify { updatable2.update(delta.toFloat()) }
          }
        }

        it("should not call the update method when the system is off") {
          val updatableSystem = UpdatableSystem()
          updatableSystem.on = false

          val entity = SimpleMockEntity()

          val updatable = mockk<Updatable> { every { update(any()) } just Runs }
          val updatableComponent = UpdatableComponent(arrayListOf(updatable))

          entity.addComponent(updatableComponent)
          updatableSystem.add(entity)

          checkAll(Arb.int(0, 100)) { delta ->
            updatableSystem.update(delta.toFloat())
            verify(exactly = 0) { updatable.update(any()) }
          }
        }
      }
    })
