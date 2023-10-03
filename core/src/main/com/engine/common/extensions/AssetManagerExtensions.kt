package com.engine.common.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Queue

/**
 * Loads all assets in a directory.
 *
 * @param directory The directory to load assets from.
 * @param type The type of the assets
 */
fun <T> AssetManager.loadAssetsInDirectory(
    directory: String,
    type: Class<T>,
) {
  val queue = Queue<String>()
  queue.addLast(directory)

  while (!queue.isEmpty) {
    val path = queue.removeFirst()
    val file = Gdx.files.internal(path)

    if (file.isDirectory) {
      file.list().forEach { queue.addLast(it.path()) }
    } else {
      load(file.path(), type)
    }
  }
}
