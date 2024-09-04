package com.mega.game.engine.common.shapes

import com.mega.game.engine.common.interfaces.IPointPositional

/** A [IGameShape2D] that has a position. */
interface PositionalGameShape2D : IGameShape2D, IPointPositional<IGameShape2D>
