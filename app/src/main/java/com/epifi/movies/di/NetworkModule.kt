package com.epifi.movies.di

import com.epifi.movies.business.data.network.NetworkDataSource
import com.epifi.movies.business.data.network.NetworkDataSourceImpl
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.network.MovieRetrofitService
import com.epifi.movies.datasource.network.MovieRetrofitServiceImpl
import com.epifi.movies.datasource.network.mappers.NetworkMapper
import com.epifi.movies.datasource.network.model.MovieNetworkEntity
import com.epifi.movies.datasource.network.retrofit.MovieApiService
import com.epifi.movies.util.EntityMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkMapper(): EntityMapper<MovieNetworkEntity, Movie> {
        return NetworkMapper()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit.Builder): MovieApiService {
        return retrofit
            .build()
            .create(MovieApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitService(
        movieApiService: MovieApiService
    ): MovieRetrofitService {
        return MovieRetrofitServiceImpl(movieApiService)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        movieRetrofitService: MovieRetrofitService,
        networkMapper: NetworkMapper
    ): NetworkDataSource {
        return NetworkDataSourceImpl(movieRetrofitService, networkMapper)
    }


}




















