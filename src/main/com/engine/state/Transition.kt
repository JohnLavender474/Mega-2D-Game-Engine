package com.engine.state

/**
 * A class representing a transition between states.
 *
 * @param T the type of elements in the state machine
 */
class Transition<T>(
    val condition: () -> Boolean,
    val nextState: State<T>
)