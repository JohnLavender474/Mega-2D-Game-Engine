package com.mega.game.engine.state

import com.mega.game.engine.common.interfaces.Resettable

/**
 * A state machine that loops through states and can branch based on conditions.
 *
 * @param T the type of elements in the state machine
 */
class StateMachine<T>(initialState: State<T>) : Resettable {

    private var currentState: State<T> = initialState

    /**
     * Sets the current state of the state machine.
     *
     * @param state the state to set
     */
    fun setState(state: State<T>) {
        currentState = state
    }

    /**
     * Advances to the next state based on the current state's transitions.
     *
     * @return the current element in the state machine
     */
    fun next(): T {
        val nextState = currentState.getNextState()
        if (nextState != null) {
            currentState = nextState
        }
        return currentState.element
    }

    /**
     * Returns the current element in the state machine.
     *
     * @return the current element in the state machine
     */
    fun getCurrent(): T = currentState.element

    /**
     * Resets the state machine to the initial state.
     */
    override fun reset() {
        // Assuming the initialState is saved somewhere if you want to reset to it
        // Alternatively, pass the initial state to the constructor and save it
    }
}
