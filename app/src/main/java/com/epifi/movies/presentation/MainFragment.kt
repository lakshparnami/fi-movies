package com.epifi.movies.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.epifi.movies.R
import com.epifi.movies.business.domain.models.Movie
import com.epifi.movies.business.domain.util.MoviePageState
import com.epifi.movies.databinding.FragmentMainBinding
import com.epifi.movies.datasource.cache.dao.MovieDao
import com.epifi.movies.presentation.recyclerview.MovieListRecyclerAdapter
import com.epifi.movies.presentation.recyclerview.PaginationScrollListener
import com.epifi.movies.util.DebouncingQueryTextListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var movieDao: MovieDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setStateEvent(MainStateEvent.GetMoviesEvent)
        initRecyclerView()
        initSearchView()
        initObservers()
    }

    private fun initObservers() {
        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                is MoviePageState.EmptyState,
                is MoviePageState.Error -> {
                    displayProgressBar(false)
                    displayError(true, it.error?.message)
                    updateRecyclerView(listOf())
                }
                is MoviePageState.Loading -> {
                    displayProgressBar(it.isInitialLoading)
                    displayError(false)
                }
                is MoviePageState.Success -> {
                    displayProgressBar(false)
                    updateRecyclerView(it.movies)
                }
            }
        }
    }

    private fun updateRecyclerView(movies: List<Movie>) {
        (binding.movieRecyclerView.adapter as? MovieListRecyclerAdapter)?.updateList(movies)
    }

    private fun initSearchView() {

        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                viewLifecycleOwner.lifecycle,

                ) {
                handleQuery(it)
            }
        )
    }

    private fun initRecyclerView() {

        val moviesAdapter = MovieListRecyclerAdapter()
        binding.movieRecyclerView.adapter = moviesAdapter
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.movieRecyclerView.layoutManager = layoutManager
        binding.movieRecyclerView.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                val query = viewModel.dataState.value?.searchQuery
                handleQuery(query)
            }

            override val isLastPage: Boolean
                get() = viewModel.dataState.value?.isLastPage ?: false
            override val isLoading: Boolean
                get() = viewModel.dataState.value?.isLoading ?: true
        })
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }


    private fun displayError(isDisplayed: Boolean, message: String? = null) {
        binding.errorView.visibility = if (isDisplayed) View.VISIBLE else View.GONE
        binding.errorMessage.visibility = if (isDisplayed) View.VISIBLE else View.GONE
        if (isDisplayed) {
            if (message != null) binding.errorMessage.text = message
            else binding.errorMessage.setText(R.string.unknown_error)
        }
    }

    fun handleQuery(searchQuery: String?) {
        if (searchQuery?.isNotEmpty() == true) {
            viewModel.setStateEvent(MainStateEvent.SearchMoviesEvent(searchQuery))
        } else {
            viewModel.setStateEvent(MainStateEvent.GetMoviesEvent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}