package com.mega.game.engine.pathfinding

import java.util.concurrent.Callable

/**
 * A pathfinder that finds a path from a start point to a target point. Implements the [Callable] interface so that it
 * can be called in a separate thread. Pathfinding is a heavy operation, so it is recommended to call this in a
 * separate thread.
 */
interface IPathfinder : Callable<PathfinderResult>
