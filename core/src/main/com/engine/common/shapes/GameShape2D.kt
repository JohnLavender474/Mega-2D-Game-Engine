package com.engine.common.shapes

import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.math.Vector2

interface GameShape2D : Shape2D {

  fun overlaps(other: GameShape2D): Boolean

  fun getBoundingRectangle(): GameRectangle

  fun getCenter(): Vector2

  fun setCenter(center: Vector2) = setCenter(center.x, center.y)

  fun setCenter(centerX: Float, centerY: Float): GameShape2D

  fun setCenterX(centerX: Float): GameShape2D

  fun setCenterY(centerY: Float): GameShape2D

  fun translation(translateX: Float, translateY: Float): GameShape2D

  fun translation(translation: Vector2) = translation(translation.x, translation.y)
}
