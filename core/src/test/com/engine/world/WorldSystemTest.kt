package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.engine.GameEntity
import com.engine.common.extensions.round
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.*

class WorldSystemTest :
    DescribeSpec({
      describe("WorldSystem class") {
        val mockContactListener = mockk<ContactListener>()
        val mockWorldGraph = mockk<WorldGraph>()
        val mockCollisionHandler = mockk<CollisionHandler>()

        val entity =
            spyk(
                object : GameEntity() {

                  override fun spawn(spawnProps: Properties) {
                    putAllProperties(spawnProps)
                  }

                  override fun runOnDeath() {}

                  override fun reset() {
                    clearComponents()
                    clearProperties()
                  }
                })

        val physicsData = spyk(PhysicsData())
        val body = spyk(Body(BodyType.DYNAMIC, physicsData))
        val bodyComponent = spyk(BodyComponent(body))

        val fixedStep = 0.02f
        val worldSystem =
            spyk(
                WorldSystem(
                    mockContactListener, { mockWorldGraph }, fixedStep, mockCollisionHandler))

        beforeEach {
          clearAllMocks()

          body.setPosition(0f, 0f)
          body.fixtures.clear()

          physicsData.resetToDefault()

          entity.reset()
          entity.putComponent(bodyComponent)

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
          every { mockWorldGraph.getBodiesOverlapping(any(), any()) } returns ArrayList()
          every { mockWorldGraph.getFixturesOverlapping(any(), any()) } returns ArrayList()
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
          every { mockWorldGraph.getBodiesOverlapping(any(), any()) } returns ArrayList()
          every { mockWorldGraph.getFixturesOverlapping(any(), any()) } returns ArrayList()
          every { worldSystem.updatePhysics(any(), any()) } just Runs
          every { worldSystem.updateFixturePositions(any()) } just Runs
          every { worldSystem.processContacts() } just Runs
          every { worldSystem.postProcess(any(), any()) } just Runs

          val fixture = Fixture(mockk(), "Type")
          body.fixtures.add(fixture)

          val bodies = ArrayList<Body>()
          val fixtures = ArrayList<Fixture>()
          every { mockWorldGraph.addBody(any()) } answers
              {
                bodies.add(firstArg())
                mockWorldGraph
              }
          every { mockWorldGraph.addFixtures(any()) } answers
              {
                fixtures.addAll(firstArg())
                mockWorldGraph
              }

          worldSystem.update(fixedStep)

          bodies shouldContain body
          fixtures shouldContain fixture
        }

        describe("cycle") {
          it("should cycle correctly - test 1") {
            every { worldSystem.preProcess(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.updatePhysics(any(), any()) } just Runs
            every { worldSystem.updateFixturePositions(any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs
            every { mockWorldGraph.addBody(any()) } returns mockWorldGraph
            every { mockWorldGraph.addFixtures(any()) } returns mockWorldGraph
            every { mockWorldGraph.reset() } just Runs

            worldSystem.update(fixedStep)

            verify(exactly = 1) { worldSystem.preProcess(any(), any()) }
            verify(exactly = 1) { worldSystem.postProcess(any(), any()) }
            verify(exactly = 1) { worldSystem.updatePhysics(any(), any()) }
            verify(exactly = 1) { worldSystem.updateFixturePositions(any()) }
            verify(exactly = 1) { worldSystem.resolveCollisions(any()) }
            verify(exactly = 1) { mockWorldGraph.addFixtures(any()) }
            verify(exactly = 1) { mockWorldGraph.addBody(any()) }
            verify(exactly = 1) { mockWorldGraph.reset() }
          }

          it("should cycle correctly - test 2") {
            every { worldSystem.preProcess(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.updatePhysics(any(), any()) } just Runs
            every { worldSystem.updateFixturePositions(any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs
            every { mockWorldGraph.addBody(any()) } returns mockWorldGraph
            every { mockWorldGraph.addFixtures(any()) } returns mockWorldGraph
            every { mockWorldGraph.reset() } just Runs

            worldSystem.update(fixedStep * 2)

            verify(exactly = 2) { worldSystem.preProcess(any(), any()) }
            verify(exactly = 2) { worldSystem.postProcess(any(), any()) }
            verify(exactly = 2) { worldSystem.updatePhysics(any(), any()) }
            verify(exactly = 2) { worldSystem.updateFixturePositions(any()) }
            verify(exactly = 2) { worldSystem.resolveCollisions(any()) }
            verify(exactly = 2) { mockWorldGraph.addFixtures(any()) }
            verify(exactly = 2) { mockWorldGraph.addBody(any()) }
            verify(exactly = 2) { mockWorldGraph.reset() }
          }
        }

        it("should filter contacts correctly") {
          val fixture1 = Fixture(mockk(), "Type1")
          val fixture2 = Fixture(mockk(), "Type2")
          val filterMap = mapOf("Type1" to setOf("Type2"))

          val filteredSystem =
              WorldSystem(
                  mockContactListener,
                  { mockWorldGraph },
                  fixedStep,
                  mockCollisionHandler,
                  filterMap)

          filteredSystem.filterContact(fixture1, fixture2) shouldBe true
          filteredSystem.filterContact(fixture1, fixture1) shouldBe false
          filteredSystem.filterContact(fixture2, fixture2) shouldBe false
        }

        describe("process contacts") {
          val fixture1 = Fixture(GameRectangle(), "Type1")
          val body1 = Body(BodyType.DYNAMIC, fixtures = arrayListOf(fixture1))
          val entity1 =
              object : GameEntity() {
                override fun spawn(spawnProps: Properties) {}

                override fun runOnDeath() {}

                override fun reset() {}
              }
          entity1.putComponent(BodyComponent(body1))

          val fixture2 = Fixture(GameRectangle(), "Type2")
          val body2 = Body(BodyType.DYNAMIC, fixtures = arrayListOf(fixture2))
          val entity2 =
              object : GameEntity() {
                override fun spawn(spawnProps: Properties) {}

                override fun runOnDeath() {}

                override fun reset() {}
              }
          entity2.putComponent(BodyComponent(body2))

          it("should process contacts correctly - test 1") {
            every { mockWorldGraph.reset() } just Runs
            every { worldSystem.processPhysicsAndGraph(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs
            every { mockContactListener.beginContact(any(), any(), any()) } just Runs
            every { mockContactListener.continueContact(any(), any(), any()) } just Runs
            every { mockContactListener.endContact(any(), any(), any()) } just Runs

            worldSystem.purge()
            worldSystem.add(entity1)
            worldSystem.add(entity2)

            every { mockWorldGraph.getFixturesOverlapping(any(), any()) } answers
                {
                  val fixture = firstArg<Fixture>()
                  if (fixture == fixture1) {
                    arrayListOf(fixture2)
                  } else {
                    arrayListOf(fixture1)
                  }
                }

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.beginContact(any(), any(), any()) }

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.continueContact(any(), any(), any()) }

            every { mockWorldGraph.getFixturesOverlapping(any(), any()) } returns arrayListOf()

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.endContact(any(), any(), any()) }
          }

          it("should process contacts correctly - test 2") {
            every { mockWorldGraph.reset() } just Runs
            every { worldSystem.processPhysicsAndGraph(any(), any()) } just Runs
            every { worldSystem.postProcess(any(), any()) } just Runs
            every { worldSystem.resolveCollisions(any()) } just Runs
            every { mockContactListener.beginContact(any(), any(), any()) } just Runs
            every { mockContactListener.continueContact(any(), any(), any()) } just Runs
            every { mockContactListener.endContact(any(), any(), any()) } just Runs

            worldSystem.purge()
            worldSystem.add(entity1)
            worldSystem.add(entity2)

            every { mockWorldGraph.getFixturesOverlapping(any(), any()) } answers
                {
                  val fixture = firstArg<Fixture>()
                  if (fixture == fixture1) {
                    arrayListOf(fixture2)
                  } else {
                    arrayListOf(fixture1)
                  }
                }

            worldSystem.update(fixedStep * 2)
            verify(exactly = 1) { mockContactListener.beginContact(any(), any(), any()) }
            verify(exactly = 1) { mockContactListener.continueContact(any(), any(), any()) }

            every { mockWorldGraph.getFixturesOverlapping(any(), any()) } returns arrayListOf()

            worldSystem.update(fixedStep)
            verify(exactly = 1) { mockContactListener.endContact(any(), any(), any()) }
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
            every { mockWorldGraph.addBody(any()) } returns mockWorldGraph
            every { mockWorldGraph.addFixtures(any()) } returns mockWorldGraph

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
          every { mockWorldGraph.addBody(any()) } returns mockWorldGraph
          every { mockWorldGraph.addFixtures(any()) } returns mockWorldGraph
          every { mockWorldGraph.getBodiesOverlapping(any(), any()) } returns ArrayList()
          every { mockWorldGraph.getFixturesOverlapping(any(), any()) } returns ArrayList()
          every { mockWorldGraph.reset() } just Runs

          val fixture = Fixture(GameRectangle(), "Type")
          fixture.offsetFromBodyCenter = Vector2(5f, 5f)
          body.fixtures.add(fixture)

          worldSystem.update(fixedStep)

          fixture.shape.getCenter() shouldBe Vector2(10f, 10f)
        }
      }
    })
