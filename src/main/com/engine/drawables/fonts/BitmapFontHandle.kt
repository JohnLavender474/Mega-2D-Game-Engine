package com.engine.drawables.fonts

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.engine.common.interfaces.Initializable
import com.engine.drawables.IDrawable

/**
 * A class that represents a bitmap font. The text is centered by default on the position by default.
 * The font can be initialized with a custom font source. The font source is a path to a .ttf file.
 * If the font source is null, then the standard font for [BitmapFont] is used. The font source can
 * either be loaded by calling [init], or else the source will be lazily loaded the first time [draw]
 * is called. If the font has already been initialized, then calling [init] does nothing. The font
 * size is given in pixels.
 *
 * @param textSupplier a supplier for the text
 * @param fontSize the size in pixels of the font. The default is 10. This is ignored if the font
 *  source is null.
 * @param position the position of the text
 * @param centerX whether the text should be centered on the x position or instead the x placed at
 *   the left; default is true
 * @param centerY whether the text should be centered on the y position or instead placed at the
 *   bottom; default is true
 * @param fontSource the source of the font; if this is null, then the standard font for
 *   [BitmapFont] is used; default is null
 */
class BitmapFontHandle(
    var textSupplier: () -> String,
    private val fontSize: Int = 10,
    val position: Vector2 = Vector2(),
    var centerX: Boolean = true,
    var centerY: Boolean = true,
    private val fontSource: String? = null,
) : Initializable, IDrawable<Batch> {

    private val layout: GlyphLayout = GlyphLayout()

    private var font: BitmapFont = BitmapFont()
    private var initialized = false

    override fun init() {
        initialized = true
        if (fontSource == null) return

        val generator = FreeTypeFontGenerator(Gdx.files.internal(fontSource))
        val parameter: FreeTypeFontGenerator.FreeTypeFontParameter =
            FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = fontSize
        font = generator.generateFont(parameter)
        generator.dispose()
    }

    override fun draw(drawer: Batch) {
        if (!initialized) init()

        layout.setText(font, textSupplier())
        val x: Float = if (centerX) position.x - layout.width / 2f else position.x
        val y: Float = if (centerY) position.y - layout.height / 2f else position.y
        font.draw(drawer, layout, x, y)
    }
}
