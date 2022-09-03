package com.epifi.movies.datasource.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.epifi.movies.datasource.cache.model.MovieCacheEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: MovieCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieCacheEntity>): List<Long>

    @Query("SELECT * FROM movies")
    suspend fun get(): List<MovieCacheEntity>

    @Query("SELECT * FROM movies where title like :name")
    suspend fun getByName(name: String): List<MovieCacheEntity>

    @Query("SELECT * FROM movies WHERE imdbID=:imdbID")
    suspend fun getById(imdbID: String): MovieCacheEntity

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query("SELECT * FROM movies WHERE page=:page LIMIT 10")
    suspend fun getByPageId(page: Int): List<MovieCacheEntity>
}