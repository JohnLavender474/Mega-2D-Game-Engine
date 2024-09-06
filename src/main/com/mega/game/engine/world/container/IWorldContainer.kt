package com.mega.game.engine.world.container

import com.mega.game.engine.common.interfaces.IClearable
import com.mega.game.engine.common.interfaces.ICopyable
import com.mega.game.engine.world.body.Body
import com.mega.game.engine.world.body.IFixture

/**
 * A container that stores and manages bodies and fixtures in the game world. It allows efficient
 * querying and retrieval of objects within specific regions or at specific points in the world.
 * This container is commonly used to optimize spatial queries for physics and collision detection.
 */
interface IWorldContainer : IClearable, ICopyable<IWorldContainer> {

    /**
     * Adds the specified body to the world container. The body is placed in the appropriate region
     * of the container based on its bounds.
     *
     * @param body the body to add
     * @return true if the body was successfully added, false otherwise
     */
    fun addBody(body: Body): Boolean

    /**
     * Adds the specified fixture to the world container. The fixture is placed in the appropriate region
     * of the container based on its bounding shape.
     *
     * @param fixture the fixture to add
     * @return true if the fixture was successfully added, false otherwise
     */
    fun addFixture(fixture: IFixture): Boolean

    /**
     * Retrieves all bodies located at the specified world coordinates (x, y).
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return an Collection Collection of bodies at the specified coordinates
     */
    fun getBodies(x: Int, y: Int): Collection<Body>

    /**
     * Retrieves all bodies within the specified rectangular area.
     *
     * @param minX the minimum x-coordinate of the area
     * @param minY the minimum y-coordinate of the area
     * @param maxX the maximum x-coordinate of the area
     * @param maxY the maximum y-coordinate of the area
     * @return an Collection Collection of bodies within the specified area
     */
    fun getBodies(minX: Int, minY: Int, maxX: Int, maxY: Int): Collection<Body>

    /**
     * Retrieves all fixtures located at the specified world coordinates (x, y).
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return an Collection Collection of fixtures at the specified coordinates
     */
    fun getFixtures(x: Int, y: Int): Collection<IFixture>

    /**
     * Retrieves all fixtures within the specified rectangular area.
     *
     * @param minX the minimum x-coordinate of the area
     * @param minY the minimum y-coordinate of the area
     * @param maxX the maximum x-coordinate of the area
     * @param maxY the maximum y-coordinate of the area
     * @return an Collection Collection of fixtures within the specified area
     */
    fun getFixtures(minX: Int, minY: Int, maxX: Int, maxY: Int): Collection<IFixture>

    /**
     * Retrieves all objects (both bodies and fixtures) located at the specified world coordinates (x, y).
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return an Collection Collection of all objects at the specified coordinates
     */
    fun getObjects(x: Int, y: Int): Collection<Any>

    /**
     * Retrieves all objects (both bodies and fixtures) within the specified rectangular area.
     *
     * @param minX the minimum x-coordinate of the area
     * @param minY the minimum y-coordinate of the area
     * @param maxX the maximum x-coordinate of the area
     * @param maxY the maximum y-coordinate of the area
     * @return an Collection Collection of all objects within the specified area
     */
    fun getObjects(minX: Int, minY: Int, maxX: Int, maxY: Int): Collection<Any>
}
