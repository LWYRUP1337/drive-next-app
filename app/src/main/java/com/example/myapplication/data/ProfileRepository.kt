package com.example.myapplication.data

import android.content.Context
import android.net.Uri
import com.example.myapplication.domain.UserProfile
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.time.Instant
import java.time.ZoneId
import kotlin.time.ExperimentalTime

class ProfileRepository(private val context: Context) {

    private val avatarFile = File(context.filesDir, "avatar.jpg")

    @OptIn(ExperimentalTime::class)
    suspend fun getProfile(): Result<UserProfile> = runCatching {
        val auth = SupabaseProvider.client.auth
        auth.awaitInitialization()
        val user = auth.currentUserOrNull() ?: auth.retrieveUserForCurrentSession(updateSession = true)
        val metadata = user.userMetadata
        UserProfile(
            firstName = metadata.text("first_name"),
            lastName = metadata.text("last_name"),
            email = user.email.orEmpty(),
            gender = metadata.text("gender"),
            joinedAt = Instant.ofEpochMilli(user.createdAt?.toEpochMilliseconds() ?: System.currentTimeMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        )
    }

    fun avatar(): File? = avatarFile.takeIf { it.exists() }

    fun saveAvatar(uri: Uri): Result<File> = runCatching {
        context.contentResolver.openInputStream(uri)!!.use { input ->
            avatarFile.outputStream().use { input.copyTo(it) }
        }
        avatarFile
    }

    suspend fun signOut() {
        runCatching { SupabaseProvider.client.auth.signOut() }
        SessionStorage(context).clearToken()
        avatarFile.delete()
    }

    private fun JsonObject?.text(key: String) = this?.get(key)?.jsonPrimitive?.contentOrNull.orEmpty()
}
