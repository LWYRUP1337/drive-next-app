package com.example.myapplication.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

object Validator {

    // name@domainname.com или .ru
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|ru)$")

    val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)

    fun isEmailValid(email: String) = emailRegex.matches(email.trim())

    fun isBirthDateValid(text: String): Boolean {
        val date = parseDate(text) ?: return false
        val today = LocalDate.now()
        return date.isBefore(today) && date.isAfter(today.minusYears(120))
    }

    fun isIssueDateValid(text: String): Boolean {
        val date = parseDate(text) ?: return false
        return !date.isAfter(LocalDate.now())
    }

    fun isLicenseNumberValid(text: String) = text.trim().length == 10

    private fun parseDate(text: String): LocalDate? = try {
        LocalDate.parse(text, dateFormatter)
    } catch (e: DateTimeParseException) {
        null
    }
}
