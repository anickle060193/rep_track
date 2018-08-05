package com.adamnickle.reptrack.model.message

class RecordingMessage( map: Map<String, Any> )
{
    val type: String by map
    val recording: Boolean by map
}