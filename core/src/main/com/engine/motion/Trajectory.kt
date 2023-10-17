package com.engine.motion

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

/**
 * Parses a string containing trajectory definitions into a list of [TrajectoryDefinition] objects.
 *
 * @param trajectoryDefinitionString The string containing trajectory definitions in the format
 *   "xVelocity,yVelocity,time".
 * @return A list of parsed trajectory definitions.
 */
fun parseTrajectoryDefinitions(trajectoryDefinitionString: String) =
    trajectoryDefinitionString.split(";".toRegex()).map {
      val values = it.split(",".toRegex())
      TrajectoryDefinition(values[0].toFloat(), values[1].toFloat(), values[2].toFloat())
    }

/**
 * A data class representing a single trajectory definition with x and y velocities and time.
 *
 * @property xVelocity The horizontal velocity.
 * @property yVelocity The vertical velocity.
 * @property time The time duration for this trajectory definition.
 */
data class TrajectoryDefinition(val xVelocity: Float, val yVelocity: Float, val time: Float)

/**
 * A class that defines a trajectory by interpolating between multiple trajectory definitions.
 *
 * @param ppm The pixels per meter (PPM) conversion factor.
 * @param trajectoryDefinitions An array of trajectory definitions.
 */
class Trajectory(
    private val ppm: Int,
    private val trajectoryDefinitions: Array<TrajectoryDefinition>
) : IMotion {

  private var currentDefinition =
      if (!trajectoryDefinitions.isEmpty) trajectoryDefinitions[0] else null
  private var duration = 0f
  private var index = 0

  /**
   * Gets the current trajectory values in pixels per second.
   *
   * @return The current trajectory values as a [Vector2] in pixels per second.
   */
  override fun getMotionValue() =
      currentDefinition?.let { Vector2(it.xVelocity, it.yVelocity).scl(ppm.toFloat()) }

  /**
   * Updates the trajectory motion based on elapsed time.
   *
   * @param delta The time elapsed since the last update.
   */
  override fun update(delta: Float) {
    duration += delta
    currentDefinition = trajectoryDefinitions[index]

    currentDefinition?.let {
      if (duration >= it.time) {
        duration = 0f
        index++

        if (index >= trajectoryDefinitions.size) {
          index = 0
        }

        currentDefinition = trajectoryDefinitions[index]
      }
    }
  }

  /** Resets the trajectory to its initial state. */
  override fun reset() {
    duration = 0f
    index = 0
  }
}
