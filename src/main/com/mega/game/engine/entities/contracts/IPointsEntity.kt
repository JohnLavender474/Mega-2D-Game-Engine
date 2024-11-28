package com.mega.game.engine.entities.contracts;

import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.points.Points;
import com.mega.game.engine.points.PointsComponent;
import kotlin.reflect.KClass;


public interface IPointsEntity extends IGameEntity {

    
    default PointsComponent getPointsComponent() {
        KClass<PointsComponent> key = ClassInstanceUtils.convertToKClass(PointsComponent.class);
        return getComponent(key);
    }

    
    default Points getPoints(Object key) {
        return getPointsComponent().getPoints(key);
    }

    
    default Points putPoints(Object key, Points points) {
        return getPointsComponent().putPoints(key, points);
    }

    
    default Points putPoints(Object key, int min, int max, int current) {
        return getPointsComponent().putPoints(key, new Points(min, max, current));
    }

    
    default Points putPoints(Object key, int value) {
        return getPointsComponent().putPoints(key, new Points(0, value, value));
    }

    
    default Points removePoints(Object key) {
        return getPointsComponent().removePoints(key);
    }
}
