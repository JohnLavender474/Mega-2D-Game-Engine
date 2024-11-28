package com.mega.game.engine.common.shapes

import com.badlogic.gdx.math.Polygon


fun Polygon.toGamePolygon() = GamePolygon(this)