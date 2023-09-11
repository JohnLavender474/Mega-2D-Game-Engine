package com.engine.drawables.sprites

import com.engine.GameComponent

/**
 * A component that can be used to store sprites.
 *
 * @property sprites The sprites.
 */
class SpriteComponent(var sprites: ArrayList<ISprite> = ArrayList()) : GameComponent
