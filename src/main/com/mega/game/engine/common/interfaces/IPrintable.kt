package com.mega.game.engine.common.interfaces

/** An interface for printing out information about an object. */
interface IPrintable {

    /** Optional method to print out information about this object. Defaults to [toString]. */
    fun print(): String = toString()
}
