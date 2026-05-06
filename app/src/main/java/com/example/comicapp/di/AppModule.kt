package com.example.comicapp.di

import android.content.Context
import androidx.room.Room
import com.example.comicapp.data.local.RickAndMortyDatabase
import com.example.comicapp.data.remote.RickAndMortyApi
import com.example.comicapp.data.repository.CharacterRepositoryImpl
import com.example.comicapp.domain.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApi(): RickAndMortyApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(RickAndMortyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RickAndMortyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRickAndMortyDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            "rickandmorty.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        api: RickAndMortyApi,
        db: RickAndMortyDatabase
    ): CharacterRepository {
        return CharacterRepositoryImpl(api, db)
    }
}
