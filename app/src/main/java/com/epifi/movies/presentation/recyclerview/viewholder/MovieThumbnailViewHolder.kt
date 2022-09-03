package com.epifi.movies.presentation.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.databinding.ViewholderMovieBinding
import com.epifi.movies.di.GlideApp

class MovieThumbnailViewHolder(private val binding: ViewholderMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {

        GlideApp
            .with(binding.movieImage.context)
            .load(movie.poster)
            .centerCrop()
            .into(binding.movieImage)

    }
}
