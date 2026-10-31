package com.example.myapplication.data

import com.example.myapplication.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

object SupabaseProvider {

    // Клиент создаётся при первом обращении; без ключей бросает понятную ошибку
    val client: SupabaseClient by lazy {
        check(BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_KEY.isNotBlank()) {
            "Supabase не настроен: укажите supabase.url и supabase.key в local.properties"
        }
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
        }
    }
}
