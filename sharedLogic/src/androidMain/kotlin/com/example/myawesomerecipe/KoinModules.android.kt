package com.example.myawesomerecipe

import io.ktor.client.HttpClient
import com.example.myawesomerecipe.cache.AndroidDatabaseDriverFactory
import com.example.myawesomerecipe.cache.DatabaseDriverFactory
import com.example.myawesomerecipe.repository.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<HttpClient> { createHttpClient() }
}