package com.mega.game.engine.screens

import com.badlogic.gdx.Screen
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.events.IEventListener

/**
 * A [IScreen] is a [Screen] that is used to display a level. It is also an [IEventListener] so that
 * it can listen for [com.mega.game.engine.events.Event]s.
 *
 * @see Screen
 * @see IEventListener
 */
interface IScreen : Screen, IPropertizable, Resettable
