package com.mega.game.engine.state

import com.mega.game.engine.common.interfaces.Resettable

/**
 * A state machine that loops through states and can branch based on conditions.
 *
 * @property initialState the initial state of the state machine
 * @property currentState the current state of the state machine
 * @property onChangeState nullable lambda which is called when the value of [currentState] changes; the first argument
 *      is the new state, and the second argument is the previous state
 * @param T the type of elements in the state machine
 */
class StateMachine<T>(
    var initialState: IState<T>,
    var onChangeState: ((currentElement: T, previousElement: T) -> Unit)? = null
) : Resettable {

    private var currentState: IState<T> = initialState

    /**
     * Sets the current state of the state machine.
     *
     * @param state the state to set
     */
    fun setState(state: IState<T>) {
        currentState = state
    }

    /**
     * Determines the next state based on which of the [currentState]'s [IState.getNextState] return value. If the
     * [currentState]'s [IState.getNextState] return value is null or equal to the [currentState] property, then the
     * value of [currentState] remains unchanged, and it is considered that no "transition" has occurred. Otherwise, a
     * "transition" is considered to have occurred, and the [onChangeState] lambda (if it is not null) is called with
     * the new state's element and the previous state's element.
     *
     * @return the current element in the state machine
     */
    fun next(): T {
        val nextState = currentState.getNextState()
        if (nextState != null && currentState != nextState) {
            val previousStateElement = currentState.element
            currentState = nextState
            onChangeState?.invoke(currentState.element, previousStateElement)
        }
        return currentState.element
    }

    /**
     * Returns the current element in the state machine.
     *
     * @return the current element in the state machine
     */
    fun getCurrent() = currentState.element

    /**
     * Resets the state machine to the initial state.
     */
    override fun reset() {
        currentState = initialState
    }
}
