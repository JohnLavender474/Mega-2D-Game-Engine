package com.engine.common.objects

data class MutablePair<A, B>(
        var first: A? = null,
        var second: B? = null
) {

    fun set(first: A?, second: B?) {
        this.first = first
        this.second = second
    }

}
