package com.epifi.movies.presentation.recyclerview


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.databinding.ViewholderMovieBinding
import com.epifi.movies.presentation.recyclerview.viewholder.MovieThumbnailViewHolder


class MovieListRecyclerAdapter :
    RecyclerView.Adapter<MovieThumbnailViewHolder>() {

    private var movies: List<Movie> = listOf()

    fun updateList(updatedMovies: List<Movie>) {
        val diffCallback = MovieDiffCallback(movies, updatedMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.movies = updatedMovies
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieThumbnailViewHolder {

        val binding =
            ViewholderMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return MovieThumbnailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieThumbnailViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    companion object {

        class MovieDiffCallback(
            private val oldList: List<Movie>,
            private val newList: List<Movie>
        ) : DiffUtil.Callback() {


            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].imdbID == newList[newItemPosition].imdbID


            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}

