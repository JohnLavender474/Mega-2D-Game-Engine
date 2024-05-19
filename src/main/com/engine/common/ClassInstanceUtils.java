package com.engine.common;

import kotlin.reflect.KClass;

/**
 * Utility class for converting between Java and Kotlin classes.
 */
public class ClassInstanceUtils {

    /**
     * Convert a Java class to a Kotlin class.
     *
     * @param clazz Java class to convert
     * @param <T>   Type of the class
     * @return Kotlin class
     */
    public static <T> KClass<T> convertToKClass(Class<T> clazz) {
        return kotlin.jvm.JvmClassMappingKt.getKotlinClass(clazz);
    }

    /**
     * Convert a Kotlin class to a Java class.
     *
     * @param clazz Kotlin class to convert
     * @param <T>   Type of the class
     * @return Java class
     */
    public static <T> Class<T> convertToClass(KClass<T> clazz) {
        return kotlin.jvm.JvmClassMappingKt.getJavaClass(clazz);
    }

}
