package com.engine.state

import com.engine.common.extensions.gdxArrayOf
import com.engine.common.extensions.objectMapOf
import java.util.function.Supplier

/**
 * A builder class for constructing a [StateMachine] with a flexible and deferred creation process.
 *
 * This builder allows states and transitions to be defined in any order. The actual creation of states
 * and transitions occurs when the [build] method is called. This ensures that transitions can be defined
 * before their respective states are added, and errors related to undefined states or transitions are only
 * thrown during the build phase.
 *
 * @param T The type of elements in the state machine.
 */
class StateMachineBuilder<T> {

    private val stateDefinitions = objectMapOf<String, T>()
    private val transitionDefinitions = gdxArrayOf<Triple<String, String, () -> Boolean>>()
    private var initialStateName: String? = null

    /**
     * Defines a state with the given name and associated element.
     *
     * @param name The name of the state.
     * @param element The element associated with the state.
     * @return The current [StateMachineBuilder] instance, allowing for method chaining.
     */
    fun state(name: String, element: T): StateMachineBuilder<T> {
        stateDefinitions.put(name, element)
        return this
    }

    /**
     * Adds a transition between two states, with a condition that determines if the transition should occur.
     *
     * @param fromState The name of the state from which the transition originates.
     * @param toState The name of the state to which the transition leads.
     * @param condition A lambda expression that returns a boolean value, indicating whether the transition should occur.
     * @return The current [StateMachineBuilder] instance, allowing for method chaining.
     */
    fun transition(fromState: String, toState: String, condition: () -> Boolean): StateMachineBuilder<T> {
        transitionDefinitions.add(Triple(fromState, toState, condition))
        return this
    }

    /**
     * Convenience version of [transition] method takes in [condition] as a [Supplier] for Java interoperability.
     * See [transition].
     *
     * @param fromState The name of the state from which the transition originates.
     * @param toState The name of the state to which the transition leads.
     * @param condition A supplier that returns a boolean value, indicating whether the transition should occur.
     */
    fun transition(fromState: String, toState: String, condition: Supplier<Boolean>) =
        transition(fromState, toState) { condition.get() }

    /**
     * Sets the initial state of the state machine. This is the state that the machine will start in.
     *
     * @param name The name of the initial state.
     * @return The current [StateMachineBuilder] instance, allowing for method chaining.
     */
    fun initialState(name: String): StateMachineBuilder<T> {
        initialStateName = name
        return this
    }

    /**
     * Builds the [StateMachine] by resolving all state and transition definitions.
     *
     * This method creates the actual [State] objects and links them according to the defined transitions.
     * If any state or transition is not properly defined, an exception is thrown at this point.
     *
     * @return The constructed [StateMachine] instance.
     * @throws IllegalStateException If the initial state is not defined.
     * @throws IllegalArgumentException If any transition references a state that has not been defined.
     */
    fun build(): StateMachine<T> {
        val states = mutableMapOf<String, State<T>>()
        stateDefinitions.forEach { states[it.key] = State(it.value) }
        val initialState = states[initialStateName]
            ?: throw IllegalStateException("Initial state $initialStateName is not defined")
        for ((fromStateName, toStateName, condition) in transitionDefinitions) {
            val fromState = states[fromStateName]
                ?: throw IllegalArgumentException("State $fromStateName not found")
            val toState = states[toStateName]
                ?: throw IllegalArgumentException("State $toStateName not found")
            fromState.addTransition(condition, toState)
        }
        return StateMachine(initialState)
    }
}
