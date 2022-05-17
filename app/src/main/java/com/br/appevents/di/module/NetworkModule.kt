package com.br.appevents.di.module

import com.br.appevents.constants.ApiConstants
import com.br.appevents.events.data.network.services.EventsService
import com.google.gson.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @Named("httpLoggingInterceptor")
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    @Named("okHttpClient")
    fun providesOkHttpClient(
        @Named("httpLoggingInterceptor") httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("retrofit")
    fun provideRetrofit(@Named("okHttpClient") okHttpClient: OkHttpClient): Retrofit {

        val gson = GsonBuilder().registerTypeAdapter(Date::class.java,
            JsonDeserializer { jsonElement: JsonElement, _: Type?, _: JsonDeserializationContext? ->
                Date(
                    jsonElement.asJsonPrimitive.asLong
                )
            } as JsonDeserializer<Date>).create()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideEventsService(@Named("retrofit") retrofit: Retrofit): EventsService {
        return retrofit.create(EventsService::class.java)
    }

}