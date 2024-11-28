package com.mega.game.engine.world.body

import com.badlogic.gdx.utils.Array
import com.mega.game.engine.common.interfaces.IDirectional
import com.mega.game.engine.common.interfaces.IProcessable
import com.mega.game.engine.common.interfaces.IRectangle
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.common.shapes.GameRectangle

interface IBody: IRectangle, IProcessable, IDirectional, Resettable {

    var physics: PhysicsData

    fun addFixture(fixture: IFixture)

    fun getFixtures(out: Array<IFixture>): Array<IFixture>

    fun forEachFixture(action: (IFixture) -> Unit)

    fun getBounds(out: GameRectangle): GameRectangle
}