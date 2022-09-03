package com.epifi.movies.datasource.cache.service

import android.util.Log
import com.epifi.movies.datasource.cache.dao.MovieDao
import com.epifi.movies.datasource.cache.model.MovieCacheEntity

class MovieDaoServiceImpl
constructor(
    private val moviesDao: MovieDao,
) : MovieDaoService {

    override suspend fun insert(movieEntity: MovieCacheEntity): Long {
        return moviesDao.insert(movieEntity)
    }

    override suspend fun insertAll(movies: List<MovieCacheEntity>): List<Long> {
        return moviesDao.insertAll(movies)
    }

    override suspend fun get(): List<MovieCacheEntity> {
        return moviesDao.get()
    }

    override suspend fun getByName(name: String): List<MovieCacheEntity> {
        return moviesDao.getByName(name)
    }

    override suspend fun getById(imdbID: String): MovieCacheEntity {
        return moviesDao.getById(imdbID)
    }

    override suspend fun clearMovies() {
        moviesDao.clearMovies()
    }

    override suspend fun getByPageId(id: Int): List<MovieCacheEntity> {
        val movies = moviesDao.getByPageId(id)
        Log.e("getByPageId", "getByPageId: $id ${movies.size}")
        return movies
    }
}