package com.example.myawesomerecipe.cache

actual fun createDriverFactory(): DatabaseDriverFactory {
    return AndroidDatabaseDriverFactory(AndroidContextHolder.applicationContext)
}