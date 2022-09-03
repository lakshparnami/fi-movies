package com.epifi.movies.datasource.network.mappers

import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.network.model.MovieNetworkEntity
import com.epifi.movies.util.EntityMapper
import javax.inject.Inject

class NetworkMapper
@Inject
constructor() : EntityMapper<MovieNetworkEntity, Movie> {

    override fun mapFromEntity(entity: MovieNetworkEntity): Movie {
        return Movie(
            title = entity.title,
            poster = entity.poster,
            imdbID = entity.imdbID,
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieNetworkEntity {
        return MovieNetworkEntity(
            title = domainModel.title,
            imdbID = domainModel.imdbID,
            poster = domainModel.poster
        )
    }


    fun mapFromEntityList(entities: List<MovieNetworkEntity>): List<Movie> {
        return entities.map { mapFromEntity(it) }
    }

}





















