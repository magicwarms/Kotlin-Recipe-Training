package com.example.myawesomerecipe.cache

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

expect fun createDriverFactory(): DatabaseDriverFactory