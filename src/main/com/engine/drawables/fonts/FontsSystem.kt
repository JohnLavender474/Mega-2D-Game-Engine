package com.engine.drawables.fonts

import com.engine.common.objects.ImmutableCollection
import com.engine.entities.IGameEntity
import com.engine.systems.GameSystem

/**
 * A system that can be used to gather the fonts to be rendered. This system will collect the fonts from
 * the entities that have a [FontsComponent] and pass them to the provided [fontsCollector].
 *
 * @param fontsCollector the function that should collect the fonts to be drawn
 */
open class FontsSystem(protected open val fontsCollector: (BitmapFontHandle) -> Unit) :
    GameSystem(FontsComponent::class) {

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

        entities.forEach { entity ->
            val fontsComponent = entity.getComponent(FontsComponent::class)
            fontsComponent?.update(delta)
            fontsComponent?.fonts?.values()?.forEach { font -> fontsCollector.invoke(font) }
        }
    }

}