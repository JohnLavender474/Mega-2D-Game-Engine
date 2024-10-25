package com.mega.game.engine.state

/**
 * Interface representing a state in a state machine.
 *
 * @param T the type of the state's element
 */
interface IState<T> {

    /**
     * The element of this state.
     */
    var element: T

    /**
     * Adds the transition to this state. A transition consists of the [condition] and its associated [nextState].
     *
     * @param condition the condition that determines whether to transition to the specified next state
     * @param nextState the next state to transition to if the condition lambda returns true
     * @return whether the transition was added or not
     */
    fun addTransition(condition: () -> Boolean, nextState: IState<T>): Boolean

    /**
     * Gets the next state from this state, or null.
     *
     * @return the next state from this state, or null
     */
    fun getNextState(): IState<T>?
}