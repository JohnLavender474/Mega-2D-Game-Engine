package com.mega.game.engine.screens

import com.badlogic.gdx.Screen
import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.interfaces.Resettable
import com.mega.game.engine.events.IEventListener


interface IScreen : Screen, IPropertizable, Resettable
