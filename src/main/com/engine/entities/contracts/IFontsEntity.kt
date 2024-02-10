package com.engine.entities.contracts

import com.engine.common.interfaces.UpdateFunction
import com.engine.drawables.fonts.BitmapFontHandle
import com.engine.drawables.fonts.FontsComponent
import com.engine.drawables.fonts.FontsSystem
import com.engine.entities.IGameEntity

/**
 * An entity that has a [FontsComponent] and can be used to store and retrieve fonts. The fonts can be used
 * to draw text on the screen. The fonts can be added, removed, and retrieved. The fonts are stored in a map
 * where the key is the key used to store the font and the value is the font itself. The fonts can be updated
 * using update functions. The fonts are updated before being collected by the [FontsSystem].
 */
interface IFontsEntity : IGameEntity {

    /**
     * Returns the [FontsComponent] of this entity.
     *
     * @return the [FontsComponent] of this entity
     */
    fun getFontsComponent() = getComponent(FontsComponent::class)!!

    /**
     * Returns the font with the given [key].
     *
     * @param key the key of the font
     * @return the font with the given [key]
     */
    fun getFont(key: Any) = getFontsComponent().fonts[key]!!

    /**
     * Adds the given [font] to this entity with the given [key].
     *
     * @param key the key of the font
     * @param font the font to add
     * @return the font that was added
     */
    fun addFont(key: Any, font: BitmapFontHandle) = getFontsComponent().fonts.put(key, font)

    /**
     * Removes the font with the given [key] from this entity.
     *
     * @param key the key of the font to remove
     * @return the font that was removed
     */
    fun removeFont(key: Any) = getFontsComponent().fonts.remove(key)

    /**
     * Puts the given [function] into this entity's [FontsComponent] with the given [key].
     *
     * @param key the key
     * @param function the function
     * @return the function that was put
     */
    fun putFontUpdateFunction(key: Any, function: UpdateFunction<BitmapFontHandle>) =
        getFontsComponent().putUpdateFunction(key, function)

    /**
     * Removes the function with the given [key] from this entity's [FontsComponent].
     *
     * @param key the key of the function to remove
     * @return the function that was removed
     */
    fun removeFontUpdateFunction(key: Any) = getFontsComponent().removeUpdateFunction(key)

}