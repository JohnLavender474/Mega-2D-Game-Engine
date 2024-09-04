package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.common.interfaces.UpdateFunction;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.fonts.BitmapFontHandle;
import com.mega.game.engine.drawables.fonts.FontsComponent;
import kotlin.reflect.KClass;

/**
 * An entity that has a FontsComponent and can be used to store and retrieve fonts. The fonts can be used
 * to draw text on the screen. The fonts can be added, removed, and retrieved. The fonts are stored in a map
 * where the key is the key used to store the font and the value is the font itself. The fonts can be updated
 * using update functions. The fonts are updated before being collected by the FontsSystem.
 */
public interface IFontsEntity extends IGameEntity {

    /**
     * Returns the FontsComponent of this entity.
     *
     * @return the FontsComponent of this entity
     */
    default FontsComponent getFontsComponent() {
        KClass<FontsComponent> key = ClassInstanceUtils.convertToKClass(FontsComponent.class);
        return getComponent(key);
    }

    /**
     * Returns the font with the given key.
     *
     * @param key the key of the font
     * @return the font with the given key
     */
    default BitmapFontHandle getFont(Object key) {
        return getFontsComponent().getFonts().get(key);
    }

    /**
     * Adds the given font to this entity with the given key.
     *
     * @param key  the key of the font
     * @param font the font to add
     * @return the font that was added
     */
    default BitmapFontHandle addFont(Object key, BitmapFontHandle font) {
        return getFontsComponent().getFonts().put(key, font);
    }

    /**
     * Removes the font with the given key from this entity.
     *
     * @param key the key of the font to remove
     * @return the font that was removed
     */
    default BitmapFontHandle removeFont(Object key) {
        return getFontsComponent().getFonts().remove(key);
    }

    /**
     * Puts the given function into this entity's FontsComponent with the given key.
     *
     * @param key      the key
     * @param function the function
     */
    default void putFontUpdateFunction(Object key, UpdateFunction<BitmapFontHandle> function) {
        getFontsComponent().putUpdateFunction(key, function);
    }

    /**
     * Removes the function with the given key from this entity's FontsComponent.
     *
     * @param key the key of the function to remove
     */
    default void removeFontUpdateFunction(Object key) {
        getFontsComponent().removeUpdateFunction(key);
    }
}

