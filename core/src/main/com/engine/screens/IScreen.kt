package com.engine.screens

import com.badlogic.gdx.Screen
import com.engine.common.interfaces.IPropertizable
import com.engine.events.IEventListener

/**
 * A [IScreen] is a [Screen] that is used to display a level. It is also an [IEventListener] so that
 * it can listen for [com.engine.events.Event]s.
 *
 * @see Screen
 * @see IEventListener
 */
interface IScreen : Screen, IEventListener, IPropertizable
