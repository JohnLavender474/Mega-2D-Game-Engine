package com.mega.game.engine.events

import com.mega.game.engine.common.interfaces.IPropertizable
import com.mega.game.engine.common.objects.Properties

/**
 * An event with an event key and properties.
 *
 * @property key the key of the event
 * @property properties the properties of the event
 */
data class Event(val key: Any, override val properties: Properties = Properties()) : IPropertizable
