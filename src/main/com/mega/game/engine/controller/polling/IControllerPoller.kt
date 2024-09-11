package com.mega.game.engine.controller.polling

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.mega.game.engine.common.interfaces.IActivatable
import com.mega.game.engine.common.interfaces.Initializable
import com.mega.game.engine.controller.buttons.ButtonStatus

/**
 * Interface for controller pollers. A controller poller polls the controller for button status and
 * returns the status of the buttons. This interface extends [IActivatable] and [Runnable] so that it
 * can be activated and run. The [run] method should update the status of each button.
 */
interface IControllerPoller : IActivatable, Runnable, Initializable {

    /**
     * Should be used to initialize this controller poller. Default implementation is a no-op.
     */
    override fun init() {
        // default implementation is a no-op
    }

    /**
     * Gets the status of the button mapped to the key.
     *
     * @param key The button key.
     * @return The status of the button mapped to the key.
     */
    fun getStatus(key: Any): ButtonStatus?

    /**
     * Returns if the button mapped to the given [key] is pressed.
     *
     * @param key The key of the button to check.
     * @return If the button mapped to the given [key] is pressed.
     */
    fun isPressed(key: Any): Boolean {
        val status = getStatus(key)
        return status == ButtonStatus.PRESSED || status == ButtonStatus.JUST_PRESSED
    }

    /**
     * Returns if all the buttons mapped to the given [keys] are pressed.
     *
     * @param keys The keys of the buttons to check.
     * @return If all the buttons mapped to the given [keys] are pressed.
     */
    fun areAllPressed(keys: Array<Any>) = keys.all { isPressed(it) }

    /**
     * Returns if any of the buttons mapped to the given [keys] are pressed.
     *
     * @param keys The keys of the buttons to check.
     * @return If any of the buttons mapped to the given [keys] are pressed.
     */
    fun isAnyPressed(keys: Array<Any>) = keys.any { isPressed(it) }

    /**
     * Returns if the button mapped to the given [key] is just pressed.
     *
     * @param key The key of the button to check.
     * @return If the button mapped to the given [key] is just pressed.
     */
    fun isJustPressed(key: Any): Boolean {
        val status = getStatus(key)
        return status == ButtonStatus.JUST_PRESSED
    }

    /**
     * Returns if all the buttons mapped to the given [keys] are just pressed.
     *
     * @param keys The keys of the buttons to check.
     * @return If all the buttons mapped to the given [keys] are just pressed.
     */
    fun areAllJustPressed(keys: Array<Any>) = keys.all { isJustPressed(it) }

    /**
     * Returns if any of the buttons mapped to the given [keys] are just pressed.
     *
     * @param keys The keys of the buttons to check.
     * @return If any of the buttons mapped to the given [keys] are just pressed.
     */
    fun isAnyJustPressed(keys: Array<Any>) = keys.any { isJustPressed(it) }

    /**
     * Returns if the button mapped to the given [key] is just released.
     *
     * @param key The key of the button to check.
     * @return If the button mapped to the given [key] is just released.
     */
    fun isJustReleased(key: Any): Boolean {
        val status = getStatus(key)
        return status == ButtonStatus.JUST_RELEASED
    }

    /**
     * Returns if all the buttons mapped to the given [keys] are just released.
     *
     * @param keys The keys of the buttons to check.
     * @return If all the buttons mapped to the given [keys] are just released.
     */
    fun areAllJustReleased(keys: Array<Any>) = keys.all { isJustReleased(it) }

    /**
     * Returns if any of the buttons mapped to the given [keys] are just released.
     *
     * @param keys The keys of the buttons to check.
     * @return If any of the buttons mapped to the given [keys] are just released.
     */
    fun isAnyJustReleased(keys: Array<Any>) = keys.any { isJustReleased(it) }

    /**
     * Returns if the button mapped to the given [key] is released.
     *
     * @param key The key of the button to check.
     * @return If the button mapped to the given [key] is released.
     */
    fun isReleased(key: Any): Boolean {
        val status = getStatus(key)
        return status == ButtonStatus.RELEASED || status == ButtonStatus.JUST_RELEASED
    }

    /**
     * Returns if all the buttons mapped to the given [keys] are released.
     *
     * @param keys The keys of the buttons to check.
     * @return If all the buttons mapped to the given [keys] are released.
     */
    fun areAllReleased(keys: Array<Any>) = keys.all { isReleased(it) }

    /**
     * Returns if any of the buttons mapped to the given [keys] are released.
     *
     * @param keys The keys of the buttons to check.
     * @return If any of the buttons mapped to the given [keys] are released.
     */
    fun isAnyReleased(keys: Array<Any>) = keys.any { isReleased(it) }

    /**
     * Returns if all the entries in the given [map] match the status of the buttons.
     *
     * @param map The map of buttons and their expected statuses.
     * @return If all the entries in the given [map] match the status of the buttons.
     */
    fun allMatch(map: ObjectMap<Any, ButtonStatus>) = map.all { getStatus(it.key) == it.value }
}
