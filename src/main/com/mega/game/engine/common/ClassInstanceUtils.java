package com.mega.game.engine.common;

import kotlin.reflect.KClass;

public class ClassInstanceUtils {
    
    public static <T> KClass<T> convertToKClass(Class<T> clazz) {
        return kotlin.jvm.JvmClassMappingKt.getKotlinClass(clazz);
    }

    public static <T> Class<T> convertToClass(KClass<T> clazz) {
        return kotlin.jvm.JvmClassMappingKt.getJavaClass(clazz);
    }

}
