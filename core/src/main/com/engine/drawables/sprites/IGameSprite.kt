package com.engine.drawables.sprites

import com.engine.common.interfaces.Positional
import com.engine.common.interfaces.Sizable

/**
 * An interface for objects that can be drawn. This interface extends the [Positional] interface and
 * the [Comparable] interface. The priority variable is used to determine the order in which sprites
 * are drawn. Sprites with a higher priority are drawn on top of sprites with a lower priority. The
 * hidden variable is used to determine whether a sprite should be drawn.
 *
 * @see Positional
 * @see Comparable
 */
interface IGameSprite : IDrawableSprite, Positional, Sizable, Comparable<IGameSprite> {

  var priority: Int
}
