package com.epifi.movies.business.data.cache

import android.util.Log
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.cache.mappers.CacheMapper
import com.epifi.movies.datasource.cache.model.MovieCacheEntity
import com.epifi.movies.datasource.cache.service.MovieDaoService

class CacheDataSourceImpl(
    private val movieDaoService: MovieDaoService,
    private val cacheMapper: CacheMapper
) : CacheDataSource {

    override suspend fun insert(movie: Movie): Long {
        return movieDaoService.insert(cacheMapper.mapToEntity(movie))
    }

    override suspend fun insertList(movies: List<Movie>, page: Int) {
        val long = movieDaoService.insertAll(cacheMapper.mapToEntityList(movies, page))
        Log.e("insertList:", "$long: Movies added to db")
    }

    override suspend fun get(): List<Movie> {
        return cacheMapper.mapFromEntityList(movieDaoService.get())
    }

    override suspend fun getByName(name: String): List<MovieCacheEntity> {
        return movieDaoService.getByName(name)
    }

    override suspend fun getByPageId(id: Int): List<Movie> {
        return cacheMapper.mapFromEntityList(movieDaoService.getByPageId(id))
    }

}
