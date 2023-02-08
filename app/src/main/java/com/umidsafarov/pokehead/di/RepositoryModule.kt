package com.umidsafarov.pokehead.di

import android.content.Context
import androidx.room.Room
import com.umidsafarov.pokehead.BuildConfig
import com.umidsafarov.pokehead.data.local.PokeheadDAO
import com.umidsafarov.pokehead.data.local.PokeheadDatabase
import com.umidsafarov.pokehead.data.remote.PokeheadService
import com.umidsafarov.pokehead.data.repository.PokemonsRepositoryImpl
import com.umidsafarov.pokehead.domain.repository.PokeheadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        PokeheadDatabase::class.java,
        BuildConfig.POKEHEAD_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providePokeheadDao(db: PokeheadDatabase) = db.pokeheadDao()

    @Singleton
    @Provides
    fun providePokemonsService(): PokeheadService {
        val httpClientBuilder = OkHttpClient.Builder()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BuildConfig.POKEHEAD_API_ENDPOINT)
            .addConverterFactory(
                GsonConverterFactory.create()
            )

        val retrofit = retrofitBuilder
            .client(
                httpClientBuilder.build()
            )
            .build()

        return retrofit.create(PokeheadService::class.java)
    }

    @Singleton
    @Provides
    fun providePokemonsRepository(
        api: PokeheadService,
        dao: PokeheadDAO,
    ): PokeheadRepository = PokemonsRepositoryImpl(api, dao)

}