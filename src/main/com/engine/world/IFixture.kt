package com.engine.world

import com.engine.common.interfaces.IPropertizable
import com.engine.common.shapes.IGameShape2D

/**
 * Defines the contract of a fixture. Fixtures are sensors attached to bodies.
 */
interface IFixture : IPropertizable {

    /**
     * Gets the shape of the fixture.
     *
     * @return the shape of the fixture.
     */
    fun getShape(): IGameShape2D

    /**
     * Gets the type of the fixture. This is used for determining collisions with other fixtures.
     *
     * @return the type of the fixture
     */
    fun getFixtureType(): Any

    /**
     * Indicates if the fixture is active or not.
     *
     * @return if the fixture is active or not
     */
    fun isActive(): Boolean
}