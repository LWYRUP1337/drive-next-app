package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.CarRepository
import com.example.myapplication.ui.cars.CarsState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = CarRepository()

    private val _state = MutableLiveData<CarsState>()
    val state: LiveData<CarsState> = _state

    init {
        load()
    }

    fun load() {
        _state.value = CarsState.Loading
        viewModelScope.launch {
            _state.value = repository.getCars().fold(
                onSuccess = { CarsState.Content(it) },
                onFailure = { CarsState.Error(R.string.error_load_cars) }
            )
        }
    }
}
