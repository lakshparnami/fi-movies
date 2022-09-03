package com.epifi.movies.presentation

import androidx.lifecycle.*
import com.epifi.movies.business.domain.util.MoviePageState
import com.epifi.movies.business.repository.MovieRepository
import com.epifi.movies.presentation.MainStateEvent.GetMoviesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState: MutableLiveData<MoviePageState> = MutableLiveData()
    val dataState: LiveData<MoviePageState>
        get() = _dataState

    fun setStateEvent(event: MainStateEvent) {
        when (event) {
            is GetMoviesEvent -> getMovies()
            is MainStateEvent.SearchMoviesEvent -> searchMovies(event.searchQuery)
        }

    }


    private fun getMovies() {
        val currentState = dataState.value
        val prevMovies =
            if (currentState?.data?.isNotEmpty() == true) {
                currentState.data
            } else {
                listOf()
            }
        viewModelScope.launch {
            movieRepository.getMovies(prevMovies)
                .onEach { dataState ->
                    _dataState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    private fun searchMovies(searchQuery: String) {
        val currentState = dataState.value
        val prevMovies =
            if (currentState?.data?.isNotEmpty() == true && currentState.searchQuery == searchQuery) {
                currentState.data
            } else {
                listOf()
            }
        viewModelScope.launch {
            movieRepository.searchMovies(searchQuery = searchQuery, prevMovies = prevMovies)
                .onEach { dataState ->
                    _dataState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}


sealed class MainStateEvent {

    object GetMoviesEvent : MainStateEvent()
    class SearchMoviesEvent(val searchQuery: String) : MainStateEvent()

}
















