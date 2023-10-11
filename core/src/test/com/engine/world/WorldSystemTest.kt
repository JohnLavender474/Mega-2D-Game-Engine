package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.engine.common.extensions.objectMapOf
import com.engine.common.extensions.objectSetOf
import com.engine.common.extensions.orderedMapOf
import com.engine.common.extensions.round
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.IGameShape2D
import com.engine.common.shapes.IGameShape2DSupplier
import com.engine.entities.GameEntity
import com.engine.graph.IGraphMap
import com.engine.graph.MinsAndMaxes
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorldSystemTest :
    DescribeSpec({
      describe("WorldSystem class") {
        val mockContactListener = mockk<IContactListener>()
        val mockWorldGraph = mockk<IGraphMap>()
        val mockWorldGraphSupplier = { mockWorldGraph }
        val mockCollisionHandler = mockk<ICollisionHandler>()

        val entity =
            spyk(
                object : GameEntity(mockk()) {

                  override fun spawn(spawnProps: Properties) {
                    putAllProperties(spawnProps)
                  }

                  override fun onDestroy() {
                    clearComponents()
                    clearProperties()
                  }
                })
        entity.dead = false

        val physicsData = spyk(PhysicsData())
        val body = spyk(Body(BodyType.DYNAMIC, physicsData))
        val bodyComponent = spyk(BodyComponent(entity, body))

        val fixedStep = 0.02f
        val worldSystem =
            spyk(
                WorldSystem(
                    mockContactListener, mockWorldGraphSupplier, fixedStep, mockCollisionHandler))

        beforeEach {
          clearAllMocks()

          body.setPosition(0f, 0f)
          body.fixtures.clear()

          physicsData.resetToDefault()

          entity.onDestroy()
          entity.addComponent(bodyComponent)

          every { mockWorldGraph.reset() } just Runs

          worldSystem.reset()
          worldSystem.add(entity)

          clearAllMocks()
        }

        it("entity should qualify") { worldSystem.qualifies(entity) shouldBe true }

        it("should contain entity") {
          every { worldSystem.process(any(), any(), any()) } just Runs
          worldSystem.update(fixedStep)
          worldSystem.contains(entity) shouldBe true
        }

        it("should pre-process entities correctly") {
          every { mockWorldGraph.reset() } just Runs
          every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()

          every { worldSystem.processPhysicsAndGraph(any(), any()) } just Runs
          every { worldSystem.updateFixturePositions(any()) } just Runs
          every { worldSystem.processContacts() } just Runs
          every { worldSystem.postProcess(any(), any()) } just Runs

          body.set(5f, 5f, 10f, 10f)
          worldSystem.update(fixedStep)

          body.previousBounds shouldBe GameRectangle(5f, 5f, 10f, 10f)
        }

        it("should add bodies and fixtures to graph correctly") {
          every { mockWorldGraph.reset() } just Runs
          every { mockWorldGraph.get(any<Body>()) } returns ObjectSet()

          every { worldSystem.updatePhysics(any(), any()) } just Runs
          every { worldSystem.updateFixturePositions(any()) } just Runs
          every { worldSystem.processContacts() } just Runs
          every { worldSystem.postProcess(any(), any()) } just Runs

          val fixture = Fixture(mockk(), "Type")
          body.fixtures.put("fixture", fixture)

          val objs = ArrayList<IGameShape2DSupplier>()

          every { mockWorldGraph.add(any()) } answers { objs.add(firstArg()) }

          worldSystem.update(fixedStep)

          objs shouldContain body
          objs shouldContain fixture
        }

        describe("cycle") {
          it("should cycle correctly - test 1") {
            body.fixtures.put("fixture", Fixture(GameRectangle(), "Test"))

            val objs = ArrayList<IGameShape2DSupplier>()

            every { worldSystem.preProcess(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.updatePhysics(any(), any()) } just Runs
            every { worldSystem.updateFixturePositions(any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs

            every { mockWorldGraph.add(any()) } answers { objs.add(firstArg()) }
            every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
            every { mockWorldGraph.reset() } just Runs

            worldSystem.update(fixedStep)

            verify(exactly = 1) { worldSystem.preProcess(any(), any()) }
            verify(exactly = 1) { worldSystem.postProcess(any(), any()) }
            verify(exactly = 1) { worldSystem.updatePhysics(any(), any()) }
            verify(exactly = 1) { worldSystem.updateFixturePositions(any()) }
            verify(exactly = 1) { worldSystem.resolveCollisions(any()) }

            verify(exactly = 1) { mockWorldGraph.reset() }
            verify(exactly = 2) { mockWorldGraph.add(any()) }

            objs.size shouldBe 2
            objs shouldContain body
            objs shouldContain body.fixtures.get("fixture")
          }

          it("should cycle correctly - test 2") {
            body.fixtures.put("fixture", Fixture(GameRectangle(), "Test"))

            val objs = ArrayList<IGameShape2DSupplier>()

            every { worldSystem.preProcess(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.updatePhysics(any(), any()) } just Runs
            every { worldSystem.updateFixturePositions(any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs

            every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
            every { mockWorldGraph.add(any()) } answers { objs.add(firstArg()) }
            every { mockWorldGraph.reset() } just Runs

            worldSystem.update(fixedStep * 2)

            verify(exactly = 2) { worldSystem.preProcess(any(), any()) }
            verify(exactly = 2) { worldSystem.postProcess(any(), any()) }
            verify(exactly = 2) { worldSystem.updatePhysics(any(), any()) }
            verify(exactly = 2) { worldSystem.updateFixturePositions(any()) }
            verify(exactly = 2) { worldSystem.resolveCollisions(any()) }

            verify(exactly = 2) { mockWorldGraph.reset() }
            verify(exactly = 4) { mockWorldGraph.add(any()) }

            objs.size shouldBe 4
            objs.filter { it == body }.size shouldBe 2
            objs.filter { it == body.fixtures.get("fixture") }.size shouldBe 2
          }
        }

        it("should filter contacts correctly") {
          val fixture1 = Fixture(mockk(), "Type1")
          val fixture2 = Fixture(mockk(), "Type2")
          val filterMap = objectMapOf("Type1" to setOf("Type2"))

          val filteredSystem =
              WorldSystem(
                  mockContactListener,
                  mockWorldGraphSupplier,
                  fixedStep,
                  mockCollisionHandler,
                  filterMap)

          filteredSystem.filterContact(fixture1, fixture2) shouldBe true
          filteredSystem.filterContact(fixture1, fixture1) shouldBe false
          filteredSystem.filterContact(fixture2, fixture2) shouldBe false
        }

        describe("process contacts") {
          val fixture1 = Fixture(GameRectangle(0f, 0f, 10f, 10f), "Type1")
          val body1 = Body(BodyType.DYNAMIC, fixtures = orderedMapOf("fixture1" to fixture1))
          val entity1 = GameEntity(mockk())
          entity1.dead = false
          entity1.addComponent(BodyComponent(entity1, body1))

          val fixture2 = Fixture(GameRectangle(5f, 5f, 15f, 15f), "Type2")
          val body2 = Body(BodyType.DYNAMIC, fixtures = orderedMapOf("fixture2" to fixture2))
          val entity2 = GameEntity(mockk())
          entity2.dead = false
          entity2.addComponent(BodyComponent(entity2, body2))

          it("should process contacts correctly - test 1") {
            every { mockWorldGraph.reset() } just Runs

            every { worldSystem.processPhysicsAndGraph(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs

            every { mockContactListener.beginContact(any(), any()) } just Runs
            every { mockContactListener.continueContact(any(), any()) } just Runs
            every { mockContactListener.endContact(any(), any()) } just Runs

            worldSystem.reset()
            worldSystem.add(entity1)
            worldSystem.add(entity2)

            every { mockWorldGraph.get(any<IGameShape2D>()) } answers
                {
                  val fixtureShape = firstArg<IGameShape2D>()
                  if (fixtureShape == fixture1.shape) {
                    objectSetOf(fixture2)
                  } else {
                    objectSetOf(fixture1)
                  }
                }

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.beginContact(any(), any()) }

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.continueContact(any(), any()) }

            every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.endContact(any(), any()) }
          }

          it("should process contacts correctly - test 2") {
            every { mockWorldGraph.reset() } just Runs

            every { worldSystem.processPhysicsAndGraph(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs

            every { mockContactListener.beginContact(any(), any()) } just Runs
            every { mockContactListener.continueContact(any(), any()) } just Runs
            every { mockContactListener.endContact(any(), any()) } just Runs

            worldSystem.reset()
            worldSystem.add(entity1)
            worldSystem.add(entity2)

            every { mockWorldGraph.get(any<IGameShape2D>()) } answers
                {
                  val fixtureShape = firstArg<IGameShape2D>()
                  if (fixtureShape == fixture1.shape) {
                    objectSetOf(fixture2)
                  } else {
                    objectSetOf(fixture1)
                  }
                }

            worldSystem.update(fixedStep * 2)
            verify(exactly = 1) { mockContactListener.beginContact(any(), any()) }
            verify(exactly = 1) { mockContactListener.continueContact(any(), any()) }

            every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.endContact(any(), any()) }
          }
        }

        describe("update physics") {
          it("should update physics correctly - test 1") {
            physicsData.gravity.x = -0.5f
            physicsData.gravity.y = -1f
            physicsData.velocity.x = 5f
            physicsData.velocity.y = 5f
            physicsData.velocityClamp.set(100f, 100f)

            worldSystem.updatePhysics(body, fixedStep)

            physicsData.velocity.x shouldBe 4.5f
            physicsData.velocity.y shouldBe 4f
            body.x.round(2) shouldBe (4.5f * fixedStep).round(2)
            body.y.round(2) shouldBe (4f * fixedStep).round(2)
          }

          it("should update physics correctly - test 2") {
            every { worldSystem.preProcess(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.updateFixturePositions(any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs

            every { mockWorldGraph.reset() } just Runs

            every { mockWorldGraph.add(any()) } returns true

            physicsData.gravity.x = -0.5f
            physicsData.gravity.y = -1f
            physicsData.velocity.x = 5f
            physicsData.velocity.y = 5f
            physicsData.velocityClamp.set(100f, 100f)

            worldSystem.update(fixedStep * 2f)

            physicsData.velocity.x shouldBe 4f
            physicsData.velocity.y shouldBe 3f
            body.x.round(2) shouldBe ((4.5f + 4f) * fixedStep).round(2)
            body.y.round(2) shouldBe ((4f + 3f) * fixedStep).round(2)
          }
        }

        it("should update fixture positions") {
          every { worldSystem.preProcess(any(), any()) } just Runs
          every { worldSystem.postProcess(any(), any()) } just Runs
          every { worldSystem.updatePhysics(any(), any()) } just Runs
          every { worldSystem.resolveCollisions(any()) } just Runs

          every { mockWorldGraph.add(any()) } returns true

          every { mockWorldGraph.get(any<MinsAndMaxes>()) } returns ObjectSet()
          every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
          every { mockWorldGraph.get(any(), any(), any(), any()) } returns ObjectSet()
          every { mockWorldGraph.get(any(), any()) } returns Array()

          every { mockWorldGraph.reset() } just Runs

          val fixture = Fixture(GameRectangle(), "Type")
          fixture.offsetFromBodyCenter = Vector2(5f, 5f)
          body.fixtures.put("fixture", fixture)

          worldSystem.update(fixedStep)

          fixture.shape.getCenter() shouldBe Vector2(10f, 10f)
        }
      }
    })
