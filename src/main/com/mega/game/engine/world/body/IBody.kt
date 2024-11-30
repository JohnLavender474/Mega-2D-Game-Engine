package com.mega.game.engine.world.body

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.*
import com.mega.game.engine.common.shapes.GameRectangle

interface IBody : IRectangle, IProcessable, IDirectional, IPropertizable, Resettable {

    var type: BodyType
    var physics: PhysicsData

    fun addFixture(fixture: IFixture)

    fun getFixtures(out: Array<IFixture>): Array<IFixture>

    fun forEachFixture(action: (IFixture) -> Unit)

    fun getBounds(out: GameRectangle): GameRectangle
}