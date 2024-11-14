package com.mega.game.engine.controller.buttons

import com.badlogic.gdx.utils.OrderedMap

/**
 * Map of controller buttons. The key can be any type; recommended to use either a String or enum type as the key.
 * Each value is an [IControllerButton] instance.
 */
class ControllerButtons : OrderedMap<Any, IControllerButton>()
