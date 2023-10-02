package com.engine.updatables

import com.badlogic.gdx.utils.Array
import com.engine.components.IGameComponent
import com.engine.common.interfaces.Updatable

/**
 * The updatable component. Contains all the updatables for an entity.
 *
 * @param updatables The updatables
 */
class UpdatablesComponent(val updatables: Array<Updatable> = Array()) : IGameComponent
