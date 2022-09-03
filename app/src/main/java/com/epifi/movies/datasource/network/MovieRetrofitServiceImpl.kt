package com.epifi.movies.datasource.network

import com.epifi.movies.datasource.network.model.MovieNetworkEntity
import com.epifi.movies.datasource.network.retrofit.MovieApiService

class MovieRetrofitServiceImpl
constructor(
    private val movieApiService: MovieApiService
) : MovieRetrofitService {

    override suspend fun getMovies(page: Int): List<MovieNetworkEntity> {
        val response = movieApiService.getMovies(page)
        println("Movies:${response.movies.size} ${response.movies}")
        return response.movies
    }

    override suspend fun searchMovies(searchQuery: String, page: Int): List<MovieNetworkEntity> {

        val response = movieApiService.getMovies(
            page = page,
            searchQuery = searchQuery
        )
        println("Movies:${response.movies.size} ${response.movies}")
        return response.movies ?: listOf()

    }
}