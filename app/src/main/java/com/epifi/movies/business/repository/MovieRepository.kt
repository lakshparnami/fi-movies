package com.epifi.movies.business.repository

import com.epifi.movies.business.data.cache.CacheDataSource
import com.epifi.movies.business.data.network.NetworkDataSource
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.business.domain.util.MoviePageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository
@Inject
constructor(
    private val networkDataSource: NetworkDataSource,
    private val cacheDataSource: CacheDataSource
) {


    fun getMovies(prevMovies: List<Movie>): Flow<MoviePageState> = flow {
        try {
            emit(MoviePageState.Loading(prevMovies = prevMovies))
            val page = (prevMovies.size / PAGE_SIZE) + 1
            val movies = ArrayList<Movie>()
            movies.addAll(prevMovies)
            val cachedMovies = cacheDataSource.getByPageId(page)
            movies.addAll(cachedMovies)
            var networkMovies: List<Movie> = listOf()
            if (cachedMovies.isEmpty()) {
                networkMovies = networkDataSource.getMovies(page)
                cacheDataSource.insertList(networkMovies, page)
                movies.addAll(networkMovies)
            }
            if (movies.isEmpty()) {
                emit(MoviePageState.EmptyState())
            } else {
                emit(
                    MoviePageState.Success(
                        movies = movies,
                        _isLastPage = networkMovies.isEmpty() && cachedMovies.isEmpty()
                    )
                )
            }
        } catch (e: Exception) {
            emit(
                MoviePageState.Error(
                    prevMovies = prevMovies,
                    exception = e
                )
            )
        }
    }

    fun searchMovies(prevMovies: List<Movie>, searchQuery: String): Flow<MoviePageState> = flow {
        try {
            emit(MoviePageState.Loading(prevMovies = prevMovies, searchQuery = searchQuery))
            val page = (prevMovies.size / PAGE_SIZE) + 1
            val movies = ArrayList<Movie>()
            movies.addAll(prevMovies)
            val networkMovies =
                networkDataSource.searchMovies(page = page, searchQuery = searchQuery)
            movies.addAll(networkMovies)
            if (movies.isEmpty()) {
                emit(MoviePageState.EmptyState())
            } else {
                emit(
                    MoviePageState.Success(
                        movies = movies,
                        _isLastPage = networkMovies.isEmpty(),
                        searchQuery = searchQuery,
                    )
                )
            }
        } catch (e: Exception) {
            emit(
                MoviePageState.Error(
                    prevMovies = prevMovies,
                    exception = e,
                )
            )
        }
    }

    companion object {
        /**
         * PAGE_SIZE should ideally be in a separate constants file
         */
        const val PAGE_SIZE = 10
    }
}
















