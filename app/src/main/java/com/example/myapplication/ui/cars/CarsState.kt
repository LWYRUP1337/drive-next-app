package com.example.myapplication.ui.cars

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.domain.Car

sealed class CarsState {
    object Loading : CarsState()
    data class Content(val cars: List<Car>) : CarsState()
    data class Error(val messageRes: Int) : CarsState()
}

fun View.renderCarsState(state: CarsState, adapter: CarAdapter) {
    findViewById<View>(R.id.progress).isVisible = state is CarsState.Loading
    findViewById<View>(R.id.errorGroup).isVisible = state is CarsState.Error
    findViewById<View>(R.id.tvEmpty).isVisible = state is CarsState.Content && state.cars.isEmpty()
    if (state is CarsState.Error) findViewById<TextView>(R.id.tvError).setText(state.messageRes)
    adapter.submit(if (state is CarsState.Content) state.cars else emptyList())
}
