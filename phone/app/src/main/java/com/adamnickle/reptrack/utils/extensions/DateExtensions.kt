package com.adamnickle.reptrack.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toShortString(): String
{
    return SimpleDateFormat.getDateInstance( SimpleDateFormat.SHORT ).format( this )
}