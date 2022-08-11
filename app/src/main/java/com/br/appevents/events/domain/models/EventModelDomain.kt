package com.br.appevents.events.domain.models

import com.br.appevents.events.presentation.models.EventModelPresentation

data class EventModelDomain(
    val people: List<Any?>,
    val date: String,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: Int
)

fun EventModelDomain.toPresentation() = EventModelPresentation(
    people = this.people,
    date = this.date,
    description = this.description,
    image = this.image,
    longitude = this.longitude,
    latitude = this.latitude,
    price = this.price,
    title = this.title,
    id = this.id
)

fun List<EventModelDomain>.toPresentation(): List<EventModelPresentation> {
    return this.map { it.toPresentation() }
}