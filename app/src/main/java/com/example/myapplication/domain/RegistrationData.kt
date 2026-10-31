package com.example.myapplication.domain

data class RegistrationData(
    val email: String,
    val password: String,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val licenseNumber: String,
    val licenseIssueDate: String
)
