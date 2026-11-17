package com.example.myapplication.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.CarRepository
import com.example.myapplication.ui.cars.CarsState
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = CarRepository()
    private var query = ""

    private val _state = MutableLiveData<CarsState>()
    val state: LiveData<CarsState> = _state

    fun search(query: String) {
        this.query = query
        retry()
    }

    fun retry() {
        _state.value = CarsState.Loading
        viewModelScope.launch {
            _state.value = repository.searchCars(query).fold(
                onSuccess = { CarsState.Content(it) },
                onFailure = { CarsState.Error(R.string.error_search) }
            )
        }
    }
}
