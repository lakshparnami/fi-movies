package com.epifi.movies.business.domain.util

import com.epifi.movies.business.domain.models.Movie


sealed class MoviePageState(
    val isLoading: Boolean,
    val isLastPage: Boolean,
    val error: Exception?,
    val data: List<Movie>,
    open val searchQuery: String?,
) {
    val isInitialLoading
        get() = isLoading && data.isEmpty()

    data class Success(
        val movies: List<Movie>,
        val _isLastPage: Boolean,
        override val searchQuery: String? = null
    ) : MoviePageState(
        data = movies,
        error = null,
        isLoading = false,
        isLastPage = _isLastPage,
        searchQuery = searchQuery,
    )

    data class Error(val prevMovies: List<Movie>, val exception: Exception) : MoviePageState(
        data = prevMovies,
        error = exception,
        isLoading = false,
        isLastPage = true,
        searchQuery = null,
    )

    data class EmptyState(override val searchQuery: String? = null) : MoviePageState(
        data = listOf(),
        error = Exception("No Movies found"),
        isLoading = false,
        isLastPage = true,
        searchQuery = null,
    )

    data class Loading(val prevMovies: List<Movie>, override val searchQuery: String? = null) :
        MoviePageState(
            data = prevMovies,
            error = null,
            isLoading = true,
            isLastPage = false,
            searchQuery = searchQuery,
        )
}
