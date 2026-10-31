package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.domain.RegistrationData
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.IOException

class AuthRepository(private val context: Context) {

    private val storage = SessionStorage(context)

    suspend fun signIn(email: String, password: String): Result<Unit> = request {
        SupabaseProvider.client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        saveSession()
    }

    // Анкета пользователя сохраняется в метаданных аккаунта (таблица auth.users)
    suspend fun signUp(data: RegistrationData): Result<Unit> = request {
        SupabaseProvider.client.auth.signUpWith(Email) {
            email = data.email
            password = data.password
            this.data = buildJsonObject {
                put("last_name", data.lastName)
                put("first_name", data.firstName)
                put("middle_name", data.middleName)
                put("birth_date", data.birthDate)
                put("gender", data.gender)
                put("license_number", data.licenseNumber)
                put("license_issue_date", data.licenseIssueDate)
            }
        }
        saveSession()
    }

    private fun saveSession() {
        val session = SupabaseProvider.client.auth.currentSessionOrNull() ?: return
        storage.saveToken(session.accessToken, System.currentTimeMillis() + session.expiresIn * 1000)
    }

    // Выполняет запрос и заменяет исключение понятным пользователю текстом
    private inline fun request(block: () -> Unit): Result<Unit> = try {
        block()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(Exception(userMessage(e), e))
    }

    private fun userMessage(e: Exception): String = when (e) {
        is AuthRestException -> when (e.errorCode) {
            AuthErrorCode.InvalidCredentials -> context.getString(R.string.error_invalid_credentials)
            AuthErrorCode.UserAlreadyExists, AuthErrorCode.EmailExists -> context.getString(R.string.error_user_exists)
            AuthErrorCode.WeakPassword -> context.getString(R.string.error_weak_password)
            AuthErrorCode.EmailNotConfirmed -> context.getString(R.string.error_email_not_confirmed)
            AuthErrorCode.EmailAddressInvalid -> context.getString(R.string.error_email)
            AuthErrorCode.OverRequestRateLimit, AuthErrorCode.OverEmailSendRateLimit -> context.getString(R.string.error_rate_limit)
            else -> e.errorDescription
        }
        is RestException -> e.description ?: e.error
        is IOException -> context.getString(R.string.error_network)
        is IllegalStateException -> e.message ?: context.getString(R.string.error_unknown)
        else -> context.getString(R.string.error_unknown)
    }
}
