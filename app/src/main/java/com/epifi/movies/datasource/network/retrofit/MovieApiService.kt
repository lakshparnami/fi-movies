package com.epifi.movies.datasource.network.retrofit

import com.epifi.movies.datasource.network.model.SearchResultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {


    /**
     * We never pass apiKey like this, this is for demonstration purpose only
     *
     * could not find movie list api for http://www.omdbapi.com/
     * Only search API exists, so added 'star wars' as search query,
     * empty query not accepted
     * was getting an error that said 'too many results'
     * so added 'plot:full' and 'type:movie' filters
     */
    @GET("/")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("s") searchQuery: String = "star wars",
//        @Query("s") searchQuery: String = "",
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = "ac2092aa",
        @Query("type") type: String = "movie"
    ): SearchResultResponse

}