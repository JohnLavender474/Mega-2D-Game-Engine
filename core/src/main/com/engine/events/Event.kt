package com.engine.events

import com.engine.common.interfaces.Propertizable
import com.engine.common.objects.Properties

/** An event with an event key and properties. */
data class Event(val eventKey: String, override val properties: Properties) : Propertizable
