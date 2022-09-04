package com.epifi.movies.business.data.network

import com.epifi.movies.business.domain.models.Movie


interface NetworkDataSource {
    suspend fun getMovies(page: Int): List<Movie>
    suspend fun searchMovies(searchQuery: String, page: Int): List<Movie>
}