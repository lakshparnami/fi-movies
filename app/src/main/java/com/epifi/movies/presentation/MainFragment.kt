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
import com.epifi.movies.util.networkObserver.NetworkObserver
import com.epifi.movies.util.networkObserver.NetworkObserverImpl
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment(), NetworkObserver by NetworkObserverImpl() {


    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var movieDao: MovieDao
    override fun onStart() {
        super.onStart()
        context?.let {
            registerNetworkObserver(
                it,
                this,
                onAvailable = { onNetworkAvailable() },
                onLost = { onNetworkLost() },
            )
        }
    }

    private fun onNetworkAvailable() {
        Snackbar.make(binding.root, "Network Available", Snackbar.LENGTH_SHORT).show()
    }

    private fun onNetworkLost() {
        Snackbar.make(binding.root, "Network Lost", Snackbar.LENGTH_INDEFINITE).show()
    }

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
        (binding.content.movieRecyclerView.adapter as? MovieListRecyclerAdapter)?.updateList(movies)
    }

    private fun initSearchView() {

        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                viewLifecycleOwner.lifecycle
            ) {
                handleQuery(it)
            }
        )
    }

    private fun initRecyclerView() {

        val moviesAdapter = MovieListRecyclerAdapter()
        binding.content.movieRecyclerView.adapter = moviesAdapter
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.content.movieRecyclerView.layoutManager = layoutManager
        binding.content.movieRecyclerView.addOnScrollListener(object :
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
        binding.loading.root.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }


    private fun displayError(isDisplayed: Boolean, message: String? = null) {
        binding.error.root.visibility = if (isDisplayed) View.VISIBLE else View.GONE
        if (isDisplayed) {
            if (message != null) binding.error.errorMessage.text = message
            else binding.error.errorMessage.setText(R.string.unknown_error)
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