package com.example.myapplication.data

import com.example.myapplication.domain.Car
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class CarRepository {

    suspend fun getCars(): Result<List<Car>> = runCatching {
        SupabaseProvider.client.from(TABLE).select {
            order("id", Order.ASCENDING)
        }.decodeList<JsonObject>().map { it.toCar() }
    }

    suspend fun searchCars(query: String): Result<List<Car>> = runCatching {
        val text = query.replace(Regex("[,()%*]"), " ").trim()
        SupabaseProvider.client.from(TABLE).select {
            filter {
                or {
                    ilike("brand", "%$text%")
                    ilike("model", "%$text%")
                }
            }
            order("id", Order.ASCENDING)
        }.decodeList<JsonObject>().map { it.toCar() }
    }

    private fun JsonObject.toCar() = Car(
        id = getValue("id").jsonPrimitive.long,
        brand = getValue("brand").jsonPrimitive.content,
        model = getValue("model").jsonPrimitive.content,
        pricePerDay = getValue("price_per_day").jsonPrimitive.int,
        transmission = getValue("transmission").jsonPrimitive.content,
        fuel = getValue("fuel").jsonPrimitive.content
    )

    private companion object {
        const val TABLE = "cars"
    }
}
