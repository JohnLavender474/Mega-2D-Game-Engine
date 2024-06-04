package com.engine.drawables.sprites

import com.badlogic.gdx.graphics.g2d.TextureAtlas

/**
 * Checks if the texture atlas contains a region with the given name.
 *
 * @param regionName The name of the region to check for.
 * @return True if the texture atlas contains a region with the given name, false otherwise.
 */
fun TextureAtlas.containsRegion(regionName: String): Boolean {
    return findRegion(regionName) != null
}