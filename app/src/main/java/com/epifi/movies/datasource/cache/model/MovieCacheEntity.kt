package com.epifi.movies.datasource.cache.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
class MovieCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "imdbID")
    var imdbID: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "poster")
    val poster: String,

    @ColumnInfo(name = "page")
    val pageNo: Int,

    ) {
    override fun toString(): String {
        return "MovieCacheEntity(imdbID='$imdbID', title='$title', poster='$poster', pageNo=$pageNo)"
    }
}



