package com.mega.game.engine.common.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mega.game.engine.common.getRandom
import com.mega.game.engine.common.objects.IntPair


fun Vector2.toIntPair() = IntPair(x.toInt(), y.toInt())


fun Vector2.swapped() = set(y, x)


fun Vector2.flipped() = set(-x, -y)


fun Vector2.toVector3(z: Float = 0f) = Vector3(x, y, z)


fun Vector2.set(value: Float): Vector2 = set(value, value)


fun Vector2.coerceX(min: Float, max: Float): Vector2 {
    x = x.coerceIn(min, max)
    return this
}


fun Vector2.coerceY(min: Float, max: Float): Vector2 {
    y = y.coerceIn(min, max)
    return this
}


fun Vector2.coerce(min: Float, max: Float): Vector2 {
    x = x.coerceIn(min, max)
    y = y.coerceIn(min, max)
    return this
}


fun Vector2.coerce(min: Vector2, max: Vector2): Vector2 {
    x = x.coerceIn(min.x, max.x)
    y = y.coerceIn(min.y, max.y)
    return this
}


fun randomVector2(min: Float, max: Float) = Vector2(getRandom(min, max), getRandom(min, max))


fun randomVector2(minX: Float, maxX: Float, minY: Float, maxY: Float) =
    Vector2(getRandom(minX, maxX), getRandom(minY, maxY))


fun vector2Of(value: Float) = Vector2(value, value)
