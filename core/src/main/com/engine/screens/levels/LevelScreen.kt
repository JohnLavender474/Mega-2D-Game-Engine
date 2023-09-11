package com.engine.screens.levels

import com.badlogic.gdx.Screen
import com.engine.events.EventListener

/**
 * A [LevelScreen] is a [Screen] that is used to display a level. It is also an [EventListener] so
 * that it can listen for [com.engine.events.Event]s.
 *
 * @see Screen
 * @see EventListener
 */
interface LevelScreen : Screen, EventListener
