package com.example.myapplication.ui.cars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.Car

class CarAdapter : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private var cars: List<Car> = emptyList()

    fun submit(items: List<Car>) {
        cars = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) = holder.bind(cars[position])

    override fun getItemCount(): Int = cars.size

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val model: TextView = itemView.findViewById(R.id.tvModel)
        private val brand: TextView = itemView.findViewById(R.id.tvBrand)
        private val price: TextView = itemView.findViewById(R.id.tvPrice)
        private val transmission: TextView = itemView.findViewById(R.id.tvTransmission)
        private val fuel: TextView = itemView.findViewById(R.id.tvFuel)

        fun bind(car: Car) {
            model.text = car.model
            brand.text = car.brand
            price.text = itemView.context.getString(R.string.price_value, car.pricePerDay)
            transmission.text = car.transmission
            fuel.text = car.fuel
        }
    }
}
