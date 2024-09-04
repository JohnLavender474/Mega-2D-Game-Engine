package com.mega.game.engine.entities.contracts;

import com.badlogic.gdx.math.Vector2;
import com.mega.game.engine.common.ClassInstanceUtils;
import com.mega.game.engine.common.interfaces.IPositional;
import com.mega.game.engine.common.interfaces.ISizable;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.entities.IGameEntity;
import com.mega.game.engine.world.Body;
import com.mega.game.engine.world.BodyComponent;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Interface for entities that have a body.
 */
public interface IBodyEntity extends IGameEntity, IPositional, ISizable {

    default Body getBody() {
        KClass<BodyComponent> key = ClassInstanceUtils.convertToKClass(BodyComponent.class);
        return Objects.requireNonNull(getComponent(key)).getBody();
    }

    default float getX() {
        return getBody().getX();
    }

    default void setX(float x) {
        getBody().setX(x);
    }

    default float getY() {
        return getBody().getY();
    }

    default void setY(float y) {
        getBody().setY(y);
    }

    default void setPosition(float x, float y) {
        getBody().setPosition(x, y);
    }

    @NotNull
    default Vector2 getPosition() {
        return getBody().getPosition();
    }

    default float getWidth() {
        return getBody().getWidth();
    }

    default void setWidth(float width) {
        getBody().setWidth(width);
    }

    default float getHeight() {
        return getBody().getHeight();
    }

    default void setHeight(float height) {
        getBody().setHeight(height);
    }

    default void setSize(float width, float height) {
        getBody().setSize(width, height);
    }

    default void translateSize(float width, float height) {
        getBody().setWidth(getBody().getWidth() + width);
        getBody().setHeight(getBody().getHeight() + height);
    }

    default void translate(float x, float y) {
        getBody().translation(x, y);
    }
}