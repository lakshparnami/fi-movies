package com.epifi.movies.datasource.network

import com.epifi.movies.datasource.network.model.MovieNetworkEntity

interface MovieRetrofitService {

    suspend fun getMovies(page: Int): List<MovieNetworkEntity>
    suspend fun searchMovies(searchQuery: String, page: Int): List<MovieNetworkEntity>
}