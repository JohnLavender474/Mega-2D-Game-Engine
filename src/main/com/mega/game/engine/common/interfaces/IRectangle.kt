package com.mega.game.engine.common.interfaces

import com.mega.game.engine.world.body.BodyType

interface IRectangle {

    var type: BodyType
    var x: Float
    var y: Float
    var width: Float
    var height: Float
}