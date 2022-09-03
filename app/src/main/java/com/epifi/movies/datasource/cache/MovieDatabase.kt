package com.epifi.movies.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.epifi.movies.datasource.cache.dao.MovieDao
import com.epifi.movies.datasource.cache.model.MovieCacheEntity


@Database(entities = [MovieCacheEntity::class], version = 4)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao


    companion object {
        const val DATABASE_NAME: String = "movie_db"
    }


}