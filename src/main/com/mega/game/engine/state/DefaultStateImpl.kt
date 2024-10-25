package com.mega.game.engine.state

import com.mega.game.engine.common.objects.MutableArray

/**
 * A simple implementation of [IState]. Contains
 *
 * @param element the element of this state
 * @param transitions the collection of [Transition]s; defaults to a [MutableArray] instance
 * @param T the type of elements in the state machine
 */
class DefaultStateImpl<T>(
    override var element: T,
    val transitions: MutableCollection<Transition<T>> = MutableArray<Transition<T>>()
) : IState<T> {

    /**
     * Adds a transition to this state that moves to the given state when the condition is met.
     *
     * @param condition the condition to check for the transition
     * @param nextState the next state to transition to
     */
    override fun addTransition(condition: () -> Boolean, nextState: IState<T>) =
        transitions.add(Transition(condition, nextState))

    /**
     * The [transitions] collection is iterated, and the next state from the first transition whose condition lambda
     * returns true will be returned.
     *
     * @return the next state if a condition is met, null otherwise
     */
    override fun getNextState(): IState<T>? =
        transitions.firstOrNull { transition -> transition.condition() }?.nextState
}