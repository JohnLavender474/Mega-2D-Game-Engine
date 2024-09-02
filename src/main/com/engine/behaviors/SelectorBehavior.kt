package com.engine.behaviors

import com.badlogic.gdx.utils.Array

/**
 * When [SelectorBehavior.evaluate] is first invoked, it iterates through the [childBehaviors] array and calls
 * [IBehavior.evaluate] on each element until one returns true.
 *
 * Once a behavior succeeds, it becomes the selected behavior. The selected behavior's [IBehavior.init],
 * [IBehavior.act], and [IBehavior.end] methods are executed based on the outcome of its [IBehavior.evaluate] method.
 *
 * If the selected behavior's [IBehavior.evaluate] method later returns false, its [IBehavior.end] method is called,
 * and the selection process starts over from the beginning of the [childBehaviors] array.
 *
 * The [childBehaviors] array should NOT be modified while the [evaluate] method is running!
 *
 * @property childBehaviors the child behaviors
 */
open class SelectorBehavior(val childBehaviors: Array<IBehavior>) : AbstractBehaviorImpl() {

    private var selectedBehavior: IBehavior? = null

    fun isBehaviorSelected() = selectedBehavior != null

    override fun evaluate(delta: Float): Boolean {
        if (selectedBehavior == null) {
            for (childBehavior in childBehaviors) {
                if (childBehavior.evaluate(delta)) {
                    selectedBehavior = childBehavior
                    return true
                }
            }
            return false
        } else return selectedBehavior!!.evaluate(delta)
    }

    override fun init() {
        selectedBehavior?.init()
    }

    override fun act(delta: Float) {
        selectedBehavior?.act(delta)
    }

    override fun end() {
        selectedBehavior = null
    }
}