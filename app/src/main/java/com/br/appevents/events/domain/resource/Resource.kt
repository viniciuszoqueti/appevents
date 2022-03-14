package com.br.appevents.events.domain.resource

class Resource<T> private constructor(
    val status: ResourceStatus,
    val data: T?,
    val message: String?
) {

    enum class ResourceStatus {
        SUCCESS,
        ERROR,
        LOADING,
        NOT_FOUND
    }

    companion object {

        fun <T> success(data: T) = Resource(
            ResourceStatus.SUCCESS,
            data,
            null
        )

        fun <T> error(data: T?, msg: String? = "") = Resource(
            ResourceStatus.ERROR,
            data,
            msg
        )

        fun <T> loading(data: T?) = Resource(
            ResourceStatus.LOADING,
            data,
            null
        )

        fun <T> notFound(data: T?) = Resource(
            ResourceStatus.NOT_FOUND,
            data,
            null
        )

    }

}

