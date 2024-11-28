package com.mega.game.engine.drawables.sprites

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap
import com.mega.game.engine.common.interfaces.Updatable
import com.mega.game.engine.common.interfaces.UpdateFunction
import com.mega.game.engine.common.objects.GamePair
import com.mega.game.engine.common.objects.pairTo
import com.mega.game.engine.components.IGameComponent


class SpritesComponent(
    val sprites: OrderedMap<String, GameSprite> = OrderedMap(),
    val updatables: ObjectMap<String, UpdateFunction<GameSprite>> = ObjectMap()
) :
    IGameComponent, Updatable {

    companion object {
        const val SPRITE = "sprite"
    }


    constructor(vararg _sprites: GamePair<String, GameSprite>) : this(OrderedMap<String, GameSprite>().apply {
        _sprites.forEach {
            put(
                it.first,
                it.second
            )
        }
    })


    constructor(sprite: GameSprite) : this(SPRITE pairTo  sprite)


    override fun update(delta: Float) {
        updatables.forEach { e ->
            val name = e.key
            val function = e.value
            sprites[name]?.let { function.update(delta, it) }
        }
    }


    fun putUpdateFunction(function: UpdateFunction<GameSprite>) {
        putUpdateFunction(SPRITE, function)
    }


    fun putUpdateFunction(key: String, function: UpdateFunction<GameSprite>) {
        updatables.put(key, function)
    }


    fun removeUpdateFunction(key: String) {
        updatables.remove(key)
    }
}
