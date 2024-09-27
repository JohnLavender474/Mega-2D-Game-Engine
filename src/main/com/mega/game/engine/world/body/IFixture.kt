package com.mega.game.engine.world.body

import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.ITypable
import com.mega.game.engine.common.shapes.IGameShape2D

/**
 * Defines the contract of a fixture. Fixtures are sensors attached to bodies.
 */
interface IFixture : ITypable, IPropertizable {

    /**
     * Gets the shape of the fixture.
     *
     * @return the shape of the fixture.
     */
    fun getShape(): IGameShape2D

    /**
     * Indicates if the fixture is active or not.
     *
     * @return if the fixture is active or not
     */
    fun isActive(): Boolean
}