package com.example.myawesomerecipe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform