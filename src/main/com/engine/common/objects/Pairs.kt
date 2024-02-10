package com.engine.common.objects

/** List of pairs of objects. */
interface IPairs<A, B> : List<Pair<A, B>>

/** Implementation for a list of pairs. Used for storing pairs of objects. */
class Pairs<A, B> : IPairs<A, B>, ArrayList<Pair<A, B>>()
