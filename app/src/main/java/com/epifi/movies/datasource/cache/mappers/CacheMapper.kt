package com.epifi.movies.datasource.cache.mappers

import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.datasource.cache.model.MovieCacheEntity
import com.epifi.movies.util.EntityMapper
import javax.inject.Inject

class CacheMapper
@Inject
constructor() : EntityMapper<MovieCacheEntity, Movie> {

    override fun mapFromEntity(entity: MovieCacheEntity): Movie {
        return Movie(
            title = entity.title,
            poster = entity.poster,
            imdbID = entity.imdbID,
        )
    }

    override fun mapToEntity(domainModel: Movie): MovieCacheEntity {
        return MovieCacheEntity(
            title = domainModel.title,
            imdbID = domainModel.imdbID,
            poster = domainModel.poster,
            pageNo = 0,
        )
    }

    private fun mapToEntity(domainModel: Movie, pageNo: Int): MovieCacheEntity {
        return MovieCacheEntity(
            title = domainModel.title,
            imdbID = domainModel.imdbID,
            poster = domainModel.poster,
            pageNo = pageNo,
        )
    }


    fun mapFromEntityList(entities: List<MovieCacheEntity>): List<Movie> {
        return entities.map { mapFromEntity(it) }
    }

    fun mapToEntityList(entities: List<Movie>, pageNo: Int): List<MovieCacheEntity> {
        return entities.map { mapToEntity(it, pageNo) }
    }

}





















