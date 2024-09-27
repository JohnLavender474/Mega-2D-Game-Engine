package com.mega.game.engine.common.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.OrderedMap

/**
 * A class that stores items that can be randomly retrieved with a given weight. The weight of an item is the probability
 * of it being selected. The weights do not need to sum to 1. In the constructor, items are added with their respective
 * weights. Items can be added or removed after construction.
 */
class WeightedRandomSelector<T>(
    vararg items: GamePair<T, Float>
) {

    private val items: OrderedMap<T, Float> = OrderedMap<T, Float>()
    private var weightSum = 0f

    init {
        items.forEach { putItem(it.first, it.second) }
    }

    /**
     * Puts an item with a specified weight. The weight must be greater than 0. If the item is already present, the weight
     * is updated. If the item is not present, it is added.
     *
     * @param item The item to put.
     * @param weight The weight of the item.
     * @throws IllegalArgumentException if the weight is less than or equal to 0.
     */
    fun putItem(item: T, weight: Float) {
        if (weight <= 0) throw IllegalArgumentException("Weight must be greater than 0")
        if (items.containsKey(item)) weightSum -= items[item]!!
        items.put(item, weight)
        weightSum += weight
    }

    /**
     * Removes an item from the selector.
     *
     * @param item The item to remove.
     */
    fun removeItem(item: T) {
        val weight = items.remove(item)
        if (weight != null) weightSum -= weight
    }

    /**
     * Returns a randomly selected item, considering the weights of each item. The probability of an item being selected
     * is proportional to its weight. The weights do not need to sum to 1. If no items are present, an IllegalStateException
     * is thrown.
     *
     * @return A randomly selected item.
     * @throws IllegalStateException if no items are present.
     */
    fun getRandomItem(): T {
        val r = MathUtils.random() * weightSum
        var cumWeight = 0f
        for (entry in items) {
            cumWeight += entry.value
            if (cumWeight >= r) return entry.key
        }
        throw IllegalStateException("No items to select from")
    }
}
