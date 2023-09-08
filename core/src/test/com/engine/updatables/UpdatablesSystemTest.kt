package com.engine.updatables

import com.engine.SimpleMockEntity
import com.engine.common.interfaces.Updatable
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.mockk.*

class UpdatablesSystemTest :
    DescribeSpec({
      describe("UpdatablesSystem") {
        it("should call the update method for each updatable in each entity") {
          val updatablesSystem = UpdatablesSystem()

          val entity1 = SimpleMockEntity()
          val entity2 = SimpleMockEntity()

          val updatable1 = mockk<Updatable> { every { update(any()) } just Runs }
          val updatable2 = mockk<Updatable> { every { update(any()) } just Runs }

          val updatablesComponent1 = UpdatablesComponent(arrayListOf(updatable1))
          val updatablesComponent2 = UpdatablesComponent(arrayListOf(updatable2))

          entity1.addComponent(updatablesComponent1)
          entity2.addComponent(updatablesComponent2)

          updatablesSystem.addAll(entity1, entity2)

          checkAll(Arb.int(0, 100)) { delta ->
            updatablesSystem.update(delta.toFloat())
            verify { updatable1.update(delta.toFloat()) }
            verify { updatable2.update(delta.toFloat()) }
          }
        }

        it("should not call the update method when the system is off") {
          val updatablesSystem = UpdatablesSystem()
          updatablesSystem.on = false

          val entity = SimpleMockEntity()

          val updatable = mockk<Updatable> { every { update(any()) } just Runs }
          val updatablesComponent = UpdatablesComponent(arrayListOf(updatable))

          entity.addComponent(updatablesComponent)
          updatablesSystem.add(entity)

          checkAll(Arb.int(0, 100)) { delta ->
            updatablesSystem.update(delta.toFloat())
            verify(exactly = 0) { updatable.update(any()) }
          }
        }
      }
    })
