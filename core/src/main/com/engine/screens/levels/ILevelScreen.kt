package com.engine.screens.levels

import com.badlogic.gdx.Screen
import com.engine.events.IEventListener

/**
 * A [ILevelScreen] is a [Screen] that is used to display a level. It is also an [IEventListener] so
 * that it can listen for [com.engine.events.Event]s.
 *
 * @see Screen
 * @see IEventListener
 */
interface ILevelScreen : Screen, IEventListener
