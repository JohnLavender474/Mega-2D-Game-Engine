package com.engine

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.engine.audio.IAudioManager
import com.engine.common.objects.Properties
import com.engine.controller.buttons.Buttons
import com.engine.controller.polling.IControllerPoller
import com.engine.events.IEventsManager

interface IGame2D : ApplicationListener {

  var batch: SpriteBatch
  var shapeRenderer: ShapeRenderer

  var buttons: Buttons
  var controllerPoller: IControllerPoller

  var assMan: AssetManager
  var audioMan: IAudioManager
  var eventsMan: IEventsManager

  var gameEngine: IGameEngine

  val disposables: Array<Disposable>

  val screens: ObjectMap<String, Screen>
  val viewports: ObjectMap<String, Viewport>
  val currentScreen: Screen?

  val properties: Properties

  fun setCurrentScreen(key: String)
}
