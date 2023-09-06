package com.engine.motion

import com.badlogic.gdx.math.Vector2

fun parseTrajectoryDefinitions(trajectoryDefinitionString: String) =
    trajectoryDefinitionString.split(";".toRegex()).map {
      val values = it.split(",".toRegex())
      TrajectoryDefinition(values[0].toFloat(), values[1].toFloat(), values[2].toFloat())
    }

data class TrajectoryDefinition(val xVelocity: Float, val yVelocity: Float, val time: Float)

class Trajectory(
    private val ppm: Int,
    private val trajectoryDefinitions: List<TrajectoryDefinition>
) : Motion {

  private var currentDefinition =
      if (trajectoryDefinitions.isNotEmpty()) trajectoryDefinitions[0] else null
  private var duration = 0f
  private var index = 0

  override fun getMotionValue() =
      currentDefinition?.let { Vector2(it.xVelocity, it.yVelocity).scl(ppm.toFloat()) }

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

  override fun reset() {
    duration = 0f
    index = 0
  }
}
