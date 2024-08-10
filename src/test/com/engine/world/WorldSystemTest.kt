package com.engine.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.engine.common.enums.Direction
import com.engine.common.extensions.objectSetOf
import com.engine.common.extensions.round
import com.engine.common.objects.Properties
import com.engine.common.shapes.GameRectangle
import com.engine.common.shapes.IGameShape2D
import com.engine.common.shapes.IGameShape2DSupplier
import com.engine.entities.GameEntity
import com.engine.entities.IGameEntity
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
            val fixedStep = 0.02f

            lateinit var entity: GameEntity
            lateinit var physicsData: PhysicsData
            lateinit var body: Body
            lateinit var bodyComponent: BodyComponent

            lateinit var contactFilterMap: ObjectMap<Any, ObjectSet<Any>>
            lateinit var worldSystem: WorldSystem

            beforeEach {
                clearAllMocks()

                entity =
                    spyk(
                        object : GameEntity() {

                            override fun spawn(spawnProps: Properties) {
                                putAllProperties(spawnProps)
                            }

                            override fun onDestroy() {
                                clearComponents()
                                clearProperties()
                            }
                        })
                entity.dead = false

                physicsData = spyk(PhysicsData())

                body = spyk(Body(BodyType.DYNAMIC, physicsData))
                body.cardinalRotation = Direction.UP
                body.setSize(0f, 0f).setPosition(0f, 0f)
                body.fixtures.clear()

                bodyComponent = spyk(BodyComponent(entity, body))
                entity.addComponent(bodyComponent)

                every { mockWorldGraph.reset() } just Runs

                contactFilterMap = ObjectMap()

                worldSystem =
                    spyk(
                        WorldSystem(
                            mockContactListener,
                            mockWorldGraphSupplier,
                            fixedStep,
                            mockCollisionHandler,
                            contactFilterMap
                        )
                    )
                worldSystem.add(entity)

                clearAllMocks()
            }

            it("entity should qualify") { worldSystem.qualifies(entity) shouldBe true }

            it("should contain entity") {
                every { worldSystem.process(any(), any(), any()) } just Runs
                worldSystem.update(fixedStep)
                worldSystem.contains(entity) shouldBe true
            }

            it("should add bodies and fixtures to graph correctly") {
                every { mockWorldGraph.reset() } just Runs
                every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()

                every { worldSystem.updatePhysics(any(), any()) } just Runs
                every { worldSystem.processContacts() } just Runs
                every { worldSystem.postProcess(any(), any()) } just Runs

                val fixture = Fixture(body, "Type", GameRectangle())
                body.addFixture(fixture)

                val objs = ArrayList<Any>()

                every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } answers
                        {
                            objs.add(firstArg())
                        }
                every { mockWorldGraph.add(any<IGameShape2DSupplier>()) } answers { objs.add(firstArg()) }

                worldSystem.update(fixedStep)

                objs shouldContain body
                objs shouldContain fixture
            }

            describe("cycle") {
                it("should cycle correctly - test 1") {
                    body.addFixture(Fixture(body, "Test", GameRectangle()))

                    val objs = ArrayList<Any>()

                    every { worldSystem.preProcess(any(), any()) } just Runs
                    every { worldSystem.postProcess(any(), any()) } just Runs
                    every { worldSystem.updatePhysics(any(), any()) } just Runs
                    every { worldSystem.resolveCollisions(any()) } just Runs

                    every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } answers
                            {
                                objs.add(firstArg())
                            }
                    every { mockWorldGraph.add(any<IGameShape2DSupplier>()) } answers
                            {
                                objs.add(firstArg())
                            }
                    every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
                    every { mockWorldGraph.reset() } just Runs

                    worldSystem.update(fixedStep)

                    verify(exactly = 1) { worldSystem.preProcess(any(), any()) }
                    verify(exactly = 1) { worldSystem.postProcess(any(), any()) }
                    verify(exactly = 1) { worldSystem.updatePhysics(any(), any()) }
                    verify(exactly = 1) { worldSystem.resolveCollisions(any()) }

                    objs.size shouldBe 4
                    objs shouldContain body
                }

                it("should cycle correctly - test 2") {
                    body.addFixture(Fixture(body, "Test", GameRectangle()))

                    val objs = ArrayList<Any>()

                    every { worldSystem.preProcess(any(), any()) } just Runs
                    every { worldSystem.postProcess(any(), any()) } just Runs
                    every { worldSystem.updatePhysics(any(), any()) } just Runs
                    every { worldSystem.resolveCollisions(any()) } just Runs

                    every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
                    every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } answers
                            {
                                objs.add(firstArg())
                            }
                    every { mockWorldGraph.add(any<IGameShape2DSupplier>()) } answers
                            {
                                objs.add(firstArg())
                            }
                    every { mockWorldGraph.reset() } just Runs

                    worldSystem.update(fixedStep * 2)

                    verify(exactly = 2) { worldSystem.preProcess(any(), any()) }
                    verify(exactly = 2) { worldSystem.postProcess(any(), any()) }
                    verify(exactly = 2) { worldSystem.updatePhysics(any(), any()) }
                    verify(exactly = 2) { worldSystem.resolveCollisions(any()) }

                    objs.size shouldBe 6
                    objs.filter { it == body }.size shouldBe 3
                }
            }

            it("should filter contacts correctly") {
                val fixture1 = Fixture(mockk(), "Type1", mockk())
                val fixture2 = Fixture(mockk(), "Type2", mockk())
                val filterMap = ObjectMap<Any, ObjectSet<Any>>()
                filterMap.put("Type1", objectSetOf("Type2"))

                val filteredSystem =
                    WorldSystem(
                        mockContactListener,
                        mockWorldGraphSupplier,
                        fixedStep,
                        mockCollisionHandler,
                        filterMap
                    )

                filteredSystem.filterContact(fixture1, fixture2) shouldBe true
                filteredSystem.filterContact(fixture1, fixture1) shouldBe false
                filteredSystem.filterContact(fixture2, fixture2) shouldBe false
            }

            describe("process contacts") {

                lateinit var entity1: IGameEntity
                lateinit var entity2: IGameEntity

                lateinit var body1: Body
                lateinit var body2: Body

                lateinit var fixture1: Fixture
                lateinit var fixture2: Fixture

                beforeEach {
                    body1 = Body(BodyType.DYNAMIC)
                    fixture1 = Fixture(body1, "Type1", GameRectangle(0f, 0f, 10f, 10f))
                    body1.addFixture(fixture1)
                    entity1 = GameEntity()
                    entity1.dead = false
                    entity1.addComponent(BodyComponent(entity1, body1))

                    body2 = Body(BodyType.DYNAMIC)
                    fixture2 = Fixture(body, "Type2", GameRectangle(5f, 5f, 15f, 15f))
                    body2.addFixture(fixture2)
                    entity2 = GameEntity()
                    entity2.dead = false
                    entity2.addComponent(BodyComponent(entity2, body2))

                    contactFilterMap.put("Type1", objectSetOf("Type2"))
                }

                it("should process contacts correctly - test 1") {
                    every { mockWorldGraph.reset() } just Runs
                    every { mockWorldGraph.add(any(), any<IGameShape2D>()) } returns true

                    every { worldSystem.updatePhysics(any(), any()) } just Runs
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
                                if (fixtureShape == fixture1.rawShape) {
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
                    every { mockWorldGraph.add(any(), any<IGameShape2D>()) } returns true

                    every { worldSystem.updatePhysics(any(), any()) } just Runs
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
                                if (fixtureShape == fixture1.rawShape) {
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
                    every { worldSystem.resolveCollisions(any()) } just Runs

                    every { mockWorldGraph.reset() } just Runs

                    every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } returns true
                    every { mockWorldGraph.add(any<IGameShape2DSupplier>()) } returns true

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

            describe("update fixture positions") {
                it("should update fixture positions - test 1") {
                    every { worldSystem.preProcess(any(), any()) } just Runs
                    every { worldSystem.postProcess(any(), any()) } just Runs
                    every { worldSystem.updatePhysics(any(), any()) } just Runs
                    every { worldSystem.resolveCollisions(any()) } just Runs

                    every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } returns true
                    every { mockWorldGraph.add(any<IGameShape2DSupplier>()) } returns true
                    every { mockWorldGraph.get(any<MinsAndMaxes>()) } returns ObjectSet()
                    every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
                    every { mockWorldGraph.get(any(), any(), any(), any()) } returns ObjectSet()
                    every { mockWorldGraph.get(any(), any()) } returns Array()
                    every { mockWorldGraph.reset() } just Runs

                    val fixture = Fixture(body, "Type", GameRectangle())
                    fixture.offsetFromBodyCenter = Vector2(5f, 5f)
                    body.addFixture(fixture)

                    worldSystem.update(fixedStep)

                    fixture.rawShape.getCenter() shouldBe Vector2(5f, 5f)
                }

                it("should update fixture positions - test 2") {
                    every { worldSystem.preProcess(any(), any()) } just Runs
                    every { worldSystem.postProcess(any(), any()) } just Runs
                    every { worldSystem.updatePhysics(any(), any()) } just Runs
                    every { worldSystem.resolveCollisions(any()) } just Runs

                    every { mockWorldGraph.add(any<Body>(), any<IGameShape2D>()) } returns true
                    every { mockWorldGraph.get(any<IGameShape2D>()) } returns ObjectSet()
                    every { mockWorldGraph.reset() } just Runs

                    val _body = Body(BodyType.DYNAMIC)
                    _body.setSize(5f).setCenter(0f, 0f)

                    val shape = GameRectangle().setSize(1f)
                    val fixture = spyk(Fixture(_body, "Type", shape))
                    fixture.offsetFromBodyCenter = Vector2(0f, 5f)

                    val _entity = GameEntity()
                    val _bodyComponent = BodyComponent(entity, _body)
                    _entity.addComponent(_bodyComponent)

                    worldSystem.remove(entity)
                    worldSystem.add(_entity)

                    _body.cardinalRotation = Direction.UP
                    worldSystem.update(fixedStep)
                    fixture.getShape().getCenter() shouldBe Vector2(0f, 5f)

                    _body.cardinalRotation = Direction.DOWN
                    worldSystem.update(fixedStep)
                    fixture.getShape().getCenter() shouldBe Vector2(0f, -5f)

                    _body.cardinalRotation = Direction.LEFT
                    worldSystem.update(fixedStep)
                    fixture.getShape().getCenter() shouldBe Vector2(-5f, 0f)

                    _body.cardinalRotation = Direction.RIGHT
                    worldSystem.update(fixedStep)

                    fixture.getShape().getCenter() shouldBe Vector2(5f, 0f)
                }
            }
        }
    })
