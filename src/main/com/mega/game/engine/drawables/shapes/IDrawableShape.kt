package com.mega.game.engine.drawables.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mega.game.engine.drawables.IDrawable


interface IDrawableShape : IDrawable<ShapeRenderer> {
    var color: Color
    var shapeType: ShapeRenderer.ShapeType
}
