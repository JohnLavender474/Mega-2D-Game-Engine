package com.engine.events

import com.engine.common.interfaces.Propertizable
import com.engine.common.objects.Properties

/** An event with an event key and properties. */
data class Event(val key: Any, override val properties: Properties = Properties()) : Propertizable
