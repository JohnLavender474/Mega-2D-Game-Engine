package com.mega.game.engine.drawables.fonts

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.mega.game.engine.common.interfaces.Initializable
import com.mega.game.engine.drawables.sorting.DrawingPriority
import com.mega.game.engine.drawables.sorting.DrawingSection
import com.mega.game.engine.drawables.sorting.IComparableDrawable


class BitmapFontHandle(
    var textSupplier: () -> String,
    private val fontSize: Int = 10,
    val position: Vector2 = Vector2(),
    var centerX: Boolean = true,
    var centerY: Boolean = true,
    var hidden: Boolean = false,
    private val fontSource: String? = null,
    override val priority: DrawingPriority = DrawingPriority(DrawingSection.FOREGROUND, 0),
) : Initializable, IComparableDrawable<Batch> {

    private val layout: GlyphLayout = GlyphLayout()
    private var font: BitmapFont = BitmapFont()
    private var initialized = false


    constructor(
        text: String,
        fontSize: Int = 10,
        position: Vector2 = Vector2(),
        centerX: Boolean = true,
        centerY: Boolean = true,
        hidden: Boolean = false,
        fontSource: String? = null,
        priority: DrawingPriority = DrawingPriority(DrawingSection.FOREGROUND, 0),
    ) : this({ text }, fontSize, position, centerX, centerY, hidden, fontSource, priority)

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
        if (hidden) return

        layout.setText(font, textSupplier())
        val x: Float = if (centerX) position.x - layout.width / 2f else position.x
        val y: Float = if (centerY) position.y - layout.height / 2f else position.y
        font.draw(drawer, layout, x, y)
    }
}
