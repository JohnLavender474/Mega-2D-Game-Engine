package com.mega.game.engine.points

import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.components.IGameComponent
import java.util.function.Consumer


class PointsComponent(
    val pointsMap: ObjectMap<Any, Points> = ObjectMap(),
    val pointsListeners: ObjectMap<Any, (Points) -> Unit> = ObjectMap()
) : IGameComponent {
    
    constructor(vararg _points: GamePair<Any, Points>) : this(_points.asIterable())
    
    constructor(_points: Iterable<GamePair<Any, Points>>) : this(ObjectMap<Any, Points>().apply {
        _points.forEach {
            put(
                it.first,
                it.second
            )
        }
    })
    
    fun getPoints(key: Any): Points = pointsMap[key]
    
    fun putPoints(key: Any, points: Points): Points? = pointsMap.put(key, points)

    fun putPoints(key: Any, min: Int, max: Int, current: Int): Points? =
        putPoints(key, Points(min, max, current))
    
    fun putPoints(key: Any, value: Int): Points? = putPoints(key, Points(0, value, value))

    fun removePoints(key: Any): Points? = pointsMap.remove(key)

    fun putListener(key: Any, listener: (Points) -> Unit) = pointsListeners.put(key, listener)

    fun putListener(key: Any, listener: Consumer<Points>) = putListener(key, listener::accept)

    fun removeListener(key: Any) = pointsListeners.remove(key)
}
