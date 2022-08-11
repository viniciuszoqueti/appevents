package com.br.appevents.events.data.network.models

import com.br.appevents.events.domain.models.EventModelDomain
import com.br.appevents.utils.extensions.convertLongToDateTime

data class EventDataResponse(
    val people: List<Any?>,
    val date: Long,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: String
)

fun EventDataResponse.toDomain() = EventModelDomain(
    people = this.people,
    date = this.date.convertLongToDateTime(),
    description = this.description,
    image = this.image,
    longitude = this.longitude,
    latitude = this.latitude,
    price = this.price,
    title = this.title,
    id = this.id.toInt()
)

fun List<EventDataResponse>.toDomain(): List<EventModelDomain> {
    return this.map { it.toDomain() }
}