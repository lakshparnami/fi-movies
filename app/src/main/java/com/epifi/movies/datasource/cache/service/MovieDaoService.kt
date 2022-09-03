package com.epifi.movies.datasource.cache.service

import com.epifi.movies.datasource.cache.model.MovieCacheEntity

interface MovieDaoService {


    suspend fun insert(movieEntity: MovieCacheEntity): Long

    suspend fun insertAll(movies: List<MovieCacheEntity>): List<Long>

    suspend fun get(): List<MovieCacheEntity>

    suspend fun getByName(name: String): List<MovieCacheEntity>

    suspend fun getById(imdbID: String): MovieCacheEntity

    suspend fun clearMovies()

    suspend fun getByPageId(id: Int): List<MovieCacheEntity>

}