package com.epifi.movies.business.domain.models

data class Movie(
    val title: String,
    val poster: String,
    var imdbID: String,
) {
    override fun toString(): String {
        return "Movie(title='$title', poster='$poster', imdbID='$imdbID')"
    }
}