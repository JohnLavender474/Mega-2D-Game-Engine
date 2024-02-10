package com.engine.common.interfaces

/** A function that returns true if the test is passed. */
interface ArgsPredicate<T> {

    /**
     * Runs the test and returns true if the test is passed.
     *
     * @param arg the argument to test
     */
    fun test(arg: T): Boolean
}
