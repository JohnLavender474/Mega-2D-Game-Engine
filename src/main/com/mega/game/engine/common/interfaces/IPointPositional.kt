package com.mega.game.engine.common.interfaces

import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.enums.Position


interface IPointPositional<T> {


    fun positionOnPoint(point: Vector2, position: Position): T


    fun getPositionPoint(position: Position): Vector2
}
