package com.engine.behaviors

import com.badlogic.gdx.utils.Array

/**
 * An implementation of [SelectorBehavior] that shuffles the [childBehaviors] array before each evaluation when there
 * is not a selected behavior.
 *
 * @property childBehaviors the child behaviors
 */
class RandomSelectorBehavior(childBehaviors: Array<IBehavior>) : SelectorBehavior(childBehaviors) {

    override fun evaluate(delta: Float): Boolean {
        if (!isBehaviorSelected()) childBehaviors.shuffle()
        return super.evaluate(delta)
    }
}