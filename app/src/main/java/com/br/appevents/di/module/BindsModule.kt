package com.br.appevents.di.module

import com.br.appevents.events.data.repositories.EventsRepositoryImpl
import com.br.appevents.events.domain.repositories.EventsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface BindsModule {
    @Binds
    fun bindsEventsRepository(eventsRepositoryImpl: EventsRepositoryImpl): EventsRepository

}