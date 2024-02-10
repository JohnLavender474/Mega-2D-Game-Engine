package com.engine.pathfinding

import com.engine.common.shapes.GameRectangle
import java.util.concurrent.Callable

/**
 * A pathfinder that finds a graphPath from a start point to a target point. Implements the [Callable]
 * interface so that it can be called in a separate thread if desired. Pathfinding is usually an
 * expensive operation, so it might be good to call this in a separate thread.
 *
 * If a graphPath is found, the [call] function should return a collection of [GameRectangle]s that
 * represent the graphPath from the start point to the target point. Otherwise, it should return null.
 */
interface IPathfinder : Callable<PathfinderResult>
