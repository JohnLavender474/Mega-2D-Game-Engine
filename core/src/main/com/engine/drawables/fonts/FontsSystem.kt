package com.engine.drawables.fonts

import com.badlogic.gdx.graphics.g2d.Batch
import com.engine.common.objects.ImmutableCollection
import com.engine.drawables.IDrawable
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the fonts to be rendered. The collection passed to the constructor
 * is used to store the fonts to be rendered. The collection is supplied by the provided supplier. The
 * collection holds [IDrawable<Batch>] objects which means that any object that implements [IDrawable<Batch>]
 * can be added to the collection. It is guaranteed that all elements collected through this system will
 * be [BitmapFontHandle] instances.
 */
open class FontsSystem(protected val fontsCollectionSupplier: () -> MutableCollection<IDrawable<Batch>>) :
    GameSystem(FontsComponent::class) {

    /**
     * Creates a [FontsSystem] where the provided [MutableCollection] is used to store the fonts to be
     * rendered.
     *
     * @param fonts the collection to store the fonts to be rendered
     */
    constructor(fonts: MutableCollection<IDrawable<Batch>>) : this({ fonts })

    /**
     * Processes the fonts to be rendered. If the system is off, nothing happens. The fonts are collected
     * into the supplied collection. The fonts are updated before being collected.
     *
     * @param on whether the system is on
     * @param entities the entities to process
     * @param delta the time in seconds since the last update
     */
    override fun process(on: Boolean, entities: ImmutableCollection<IGameEntity>, delta: Float) {
        if (!on) return

        val fonts = fontsCollectionSupplier()
        entities.forEach { entity ->
            val fontsComponent = entity.getComponent(FontsComponent::class)
            fontsComponent?.update(delta)
            fontsComponent?.fonts?.values()?.forEach { font -> fonts.add(font) }
        }
    }

}