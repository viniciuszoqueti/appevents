package com.br.appevents.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertLongToDateTime(): String {
    val date = Date(this@convertLongToDateTime)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return format.format(date)
}