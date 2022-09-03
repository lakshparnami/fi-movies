package com.epifi.movies.di

import android.content.Context
import androidx.room.Room
import com.epifi.movies.business.data.cache.CacheDataSource
import com.epifi.movies.business.data.cache.CacheDataSourceImpl
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.cache.MovieDatabase
import com.epifi.movies.datasource.cache.dao.MovieDao
import com.epifi.movies.datasource.cache.mappers.CacheMapper
import com.epifi.movies.datasource.cache.model.MovieCacheEntity
import com.epifi.movies.datasource.cache.service.MovieDaoService
import com.epifi.movies.datasource.cache.service.MovieDaoServiceImpl
import com.epifi.movies.util.EntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideCacheMapper(): EntityMapper<MovieCacheEntity, Movie> {
        return CacheMapper()
    }

    @Singleton
    @Provides
    fun provideMovieDb(@ApplicationContext context: Context): MovieDatabase {
        return Room
            .databaseBuilder(
                context,
                MovieDatabase::class.java,
                MovieDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDAO(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.moviesDao()
    }

    @Singleton
    @Provides
    fun provideMovieDaoService(
        moviesDao: MovieDao,

        ): MovieDaoService {
        return MovieDaoServiceImpl(moviesDao)
    }


    @Singleton
    @Provides
    fun provideCacheDataSource(
        movieDaoService: MovieDaoService,
        cacheMapper: CacheMapper
    ): CacheDataSource {
        return CacheDataSourceImpl(movieDaoService, cacheMapper)
    }


}

























