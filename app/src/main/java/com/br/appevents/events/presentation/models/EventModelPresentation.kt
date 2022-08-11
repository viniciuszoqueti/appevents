package com.br.appevents.events.presentation.models

import android.net.Uri

data class EventModelPresentation(
    val people: List<Any?>,
    val date: String,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: Int
) {
    fun getMapFormattedUri(): Uri? {
        return Uri.parse(
            "geo:" + latitude + "," +
                    longitude + "?q=" + latitude + "," +
                    longitude
        )
    }
}