package com.engine.drawables.sprites

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent

/**
 * A component that can be used to store sprites.
 *
 * @property sprites The sprites.
 */
class SpriteComponent(var sprites: Array<IGameSprite> = Array()) : IGameComponent
