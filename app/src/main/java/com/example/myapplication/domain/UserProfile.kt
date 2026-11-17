package com.example.myapplication.domain

import java.time.LocalDate

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String,
    val joinedAt: LocalDate
)
