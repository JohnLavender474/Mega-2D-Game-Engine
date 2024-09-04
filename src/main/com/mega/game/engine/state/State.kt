package com.mega.game.engine.state

import com.badlogic.gdx.utils.Array

/**
 * A simple state class representing a node in the state machine.
 *
 * @param T the type of elements in the state machine
 */
class State<T>(val element: T, val transitions: Array<Transition<T>> = Array<Transition<T>>()) {

    /**
     * Adds a transition to this state that moves to the given state when the condition is met.
     *
     * @param condition the condition to check for the transition
     * @param nextState the next state to transition to
     */
    fun addTransition(condition: () -> Boolean, nextState: State<T>) = transitions.add(Transition(condition, nextState))

    /**
     * Gets the next state based on the conditions of the transitions. The [transitions] array is iterated based on
     * insertion order. Manipulation of this array should not be done from within any [Transition.condition] or
     * [Transition.nextState] method logic.
     *
     * @return the next state if a condition is met, null otherwise
     */
    fun getNextState(): State<T>? {
        for (transition in transitions) if (transition.condition()) return transition.nextState
        return null
    }
}