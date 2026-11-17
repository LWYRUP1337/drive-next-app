package com.example.myapplication.domain

data class Car(
    val id: Long,
    val brand: String,
    val model: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuel: String
)
