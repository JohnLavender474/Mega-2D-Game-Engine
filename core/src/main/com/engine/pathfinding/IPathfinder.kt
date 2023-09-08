package com.engine.pathfinding

import com.engine.common.objects.IntPair
import com.engine.common.shapes.GameRectangle
import java.util.concurrent.Callable

/**
 * A pathfinder that finds a path from a start point to a target point. Implements the [Callable]
 * interface so that it can be called in a separate thread if desired. Pathfinding is usually an
 * expensive operation, so it might be good to call this in a separate thread.
 *
 * If a path is found, the [call] function should return a collection of [GameRectangle]s that
 * represent the path from the start point to the target point. Otherwise, it should return null.
 */
interface IPathfinder : Callable<Collection<IntPair>?>
