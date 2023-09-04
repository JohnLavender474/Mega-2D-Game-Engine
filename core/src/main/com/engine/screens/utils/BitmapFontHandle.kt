package com.engine.screens.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Drawable

/**
 * A class that represents a bitmap font. The text is centered by default.
 *
 * @param position the position of the text
 * @param textSupplier a supplier for the text
 * @param fontSource the source of the font
 * @param fontSize the size of the font
 * @param centerX whether the text should be centered on the x-axis
 * @param centerY whether the text should be centered on the y-axis
 */
class BitmapFontHandle(
    val position: Vector2,
    var textSupplier: () -> String,
    fontSource: String,
    fontSize: Int,
    var centerX: Boolean = true,
    var centerY: Boolean = true
) : Drawable {

  private var font: BitmapFont = BitmapFont()
  private val layout: GlyphLayout = GlyphLayout()

  init {
    val generator = FreeTypeFontGenerator(Gdx.files.internal(fontSource))
    val parameter: FreeTypeFontGenerator.FreeTypeFontParameter =
        FreeTypeFontGenerator.FreeTypeFontParameter()
    parameter.size = fontSize
    font = generator.generateFont(parameter)
    generator.dispose()
  }

  override fun draw(batch: Batch) {
    layout.setText(font, textSupplier())
    val x: Float = if (centerX) position.x - layout.width / 2f else position.x
    val y: Float = if (centerY) position.y - layout.height / 2f else position.y
    font.draw(batch, layout, x, y)
  }
}
