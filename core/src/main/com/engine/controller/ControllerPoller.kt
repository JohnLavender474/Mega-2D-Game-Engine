package com.engine.controller

/**
 * The polling option for the controller poller.
 *
 * KEYBOARD: Only poll the keyboard. CONTROLLER: Only poll the controller. BOTH: Poll both the
 * keyboard and the controller.
 */
enum class ControllerPollingOption {
  KEYBOARD,
  CONTROLLER,
  BOTH
}

/**
 * Polls the controller, i.e. checks the status of each controller button. The collection of buttons
 * passed into the constructor is internally converted into a map. The key is the name of the button
 * and the value is the button itself. The controller should be polled every frame.
 *
 * @param buttons The collection of buttons to manage.
 */
class ControllerPoller(
    buttons: Iterable<Pair<String, IControllerButtonPoller>>,
    var pollingOption: ControllerPollingOption = ControllerPollingOption.BOTH
) : IControllerPoller {

  /**
   * Handle class for a controller button. Contains the button itself and the status of the button.
   *
   * @param status The status of the button.
   */
  internal data class ControllerButtonHandle(
      internal val poller: IControllerButtonPoller,
      internal var status: ControllerButtonStatus = ControllerButtonStatus.RELEASED
  )

  internal val buttonHandles =
      buttons.associate { (name, poller) -> name to ControllerButtonHandle(poller) }

  // if the poller should run
  override var on = true

  /**
   * Gets the status of the button mapped to the key.
   *
   * @param name The button name.
   * @return The status of the button mapped to the key.
   */
  override fun getButtonStatus(name: String) = buttonHandles[name]?.status

  /**
   * Runs the controller poller. This should be called every frame. This will update the status of
   * each button. If a button is pressed, the status will be updated to JUST_PRESSED or PRESSED. If
   * a button is released, the status will be updated to JUST_RELEASED or RELEASED. Otherwise, the
   * status will remain the same.
   */
  override fun run() {
    if (!on) return
    buttonHandles.values.forEach {
      val (poller, status) = it

      val pressed =
          when (pollingOption) {
            ControllerPollingOption.KEYBOARD -> poller.isKeyboardButtonPressed()
            ControllerPollingOption.CONTROLLER -> poller.isControllerButtonPressed(0)
            ControllerPollingOption.BOTH ->
                poller.isKeyboardButtonPressed() || poller.isControllerButtonPressed(0)
          }

      if (pressed) {
        when (status) {
          ControllerButtonStatus.RELEASED,
          ControllerButtonStatus.JUST_RELEASED -> it.status = ControllerButtonStatus.JUST_PRESSED
          else -> it.status = ControllerButtonStatus.PRESSED
        }
      } else if (status == ControllerButtonStatus.RELEASED ||
          status == ControllerButtonStatus.JUST_RELEASED) {
        it.status = ControllerButtonStatus.RELEASED
      } else {
        it.status = ControllerButtonStatus.JUST_RELEASED
      }
    }
  }
}
