package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.common.interfaces.UpdateFunction;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.drawables.fonts.BitmapFontHandle;
import com.mega.game.engine.drawables.fonts.FontsComponent;
import kotlin.reflect.KClass;


public interface IFontsEntity extends IGameEntity {


    default FontsComponent getFontsComponent() {
        KClass<FontsComponent> key = ClassInstanceUtils.convertToKClass(FontsComponent.class);
        return getComponent(key);
    }


    default BitmapFontHandle getFont(Object key) {
        return getFontsComponent().getFonts().get(key);
    }


    default BitmapFontHandle addFont(Object key, BitmapFontHandle font) {
        return getFontsComponent().getFonts().put(key, font);
    }


    default BitmapFontHandle removeFont(Object key) {
        return getFontsComponent().getFonts().remove(key);
    }


    default void putFontUpdateFunction(Object key, UpdateFunction<BitmapFontHandle> function) {
        getFontsComponent().putUpdateFunction(key, function);
    }


    default void removeFontUpdateFunction(Object key) {
        getFontsComponent().removeUpdateFunction(key);
    }
}

