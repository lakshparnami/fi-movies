package com.epifi.movies.business.data.network


import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.network.MovieRetrofitService
import com.epifi.movies.datasource.network.mappers.NetworkMapper

class NetworkDataSourceImpl
constructor(
    private val movieRetrofitService: MovieRetrofitService,
    private val networkMapper: NetworkMapper
) : NetworkDataSource {

    override suspend fun getMovies(page: Int): List<Movie> {
        val movies = movieRetrofitService.getMovies(page)
        return networkMapper.mapFromEntityList(movies)
    }

    override suspend fun searchMovies(searchQuery: String, page: Int): List<Movie> {
        val movies = movieRetrofitService.searchMovies(searchQuery = searchQuery, page = page)
        return networkMapper.mapFromEntityList(movies)
    }

}