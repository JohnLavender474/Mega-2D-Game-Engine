package com.engine.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport

/**
 * A class that represents both a [Stage] and a [Screen]. This class is used to create screens that
 * use the functionality of [Stage].
 *
 * @param viewport the viewport of the stage
 * @param batch the batch of the stage
 * @see Stage
 * @see Screen
 */
abstract class StageScreen(viewport: Viewport, batch: Batch) : Stage(viewport, batch), Screen
