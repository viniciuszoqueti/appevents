package com.br.appevents.events.domain.resource

sealed class Resource<T> private constructor() {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val msg: String?) : Resource<T>()
    class Loading<T> : Resource<T>()
    class NotFound<T>: Resource<T>()
}

