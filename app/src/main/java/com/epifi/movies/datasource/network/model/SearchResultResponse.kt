package com.epifi.movies.datasource.network.model

import com.google.gson.annotations.SerializedName

data class SearchResultResponse(
    @SerializedName("Search")
    var movies: List<MovieNetworkEntity> = arrayListOf(),
    @SerializedName("totalResults")
    var totalResults: Int = 0
)