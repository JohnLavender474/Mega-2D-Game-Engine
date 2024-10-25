package com.mega.game.engine.state

/**
 * A class representing a transition between states.
 *
 * @param condition the condition that determines whether to act on this transition
 * @param nextState the next state of this transition
 * @param T the type of elements in the state machine
 */
class Transition<T>(val condition: () -> Boolean, val nextState: IState<T>)