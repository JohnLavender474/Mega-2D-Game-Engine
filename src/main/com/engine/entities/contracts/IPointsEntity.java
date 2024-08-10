package com.engine.entities.contracts;

import com.engine.common.ClassInstanceUtils;
import com.engine.entities.IGameEntity;
import com.engine.points.Points;
import com.engine.points.PointsComponent;
import kotlin.reflect.KClass;

/**
 * An entity containing points.
 */
public interface IPointsEntity extends IGameEntity {

    /**
     * Returns the PointsComponent of this IGameEntity.
     *
     * @return The PointsComponent.
     */
    default PointsComponent getPointsComponent() {
        KClass<PointsComponent> key = ClassInstanceUtils.convertToKClass(PointsComponent.class);
        return getComponent(key);
    }

    /**
     * Gets the points mapped to the given key.
     *
     * @param key The key of the points.
     * @return The points.
     */
    default Points getPoints(Object key) {
        return getPointsComponent().getPoints(key);
    }

    /**
     * Puts the points into the map.
     *
     * @param key    The key of the points.
     * @param points The points.
     * @return The previous points mapped to the given key if any.
     */
    default Points putPoints(Object key, Points points) {
        return getPointsComponent().putPoints(key, points);
    }

    /**
     * Puts the points into the map.
     *
     * @param key     The key of the points.
     * @param min     The minimum value.
     * @param max     The maximum value.
     * @param current The current value.
     * @return The previous points mapped to the given key if any.
     */
    default Points putPoints(Object key, int min, int max, int current) {
        return getPointsComponent().putPoints(key, new Points(min, max, current));
    }

    /**
     * Puts the points into the map. The min value will be zero. The current and max values will be
     * equal to value.
     *
     * @param key   The key of the points.
     * @param value The value.
     * @return The previous points mapped to the given key if any.
     */
    default Points putPoints(Object key, int value) {
        return getPointsComponent().putPoints(key, new Points(0, value, value));
    }

    /**
     * Removes the points mapped to the given key.
     *
     * @param key The key of the points.
     * @return The removed points.
     */
    default Points removePoints(Object key) {
        return getPointsComponent().removePoints(key);
    }
}
