package com.epifi.movies.business.data.cache

import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.cache.model.MovieCacheEntity


interface CacheDataSource {

    suspend fun insert(movie: Movie): Long

    suspend fun insertList(movies: List<Movie>, page: Int)

    suspend fun get(): List<Movie>

    suspend fun getByName(name: String): List<MovieCacheEntity>

    suspend fun getByPageId(id: Int): List<Movie>
}