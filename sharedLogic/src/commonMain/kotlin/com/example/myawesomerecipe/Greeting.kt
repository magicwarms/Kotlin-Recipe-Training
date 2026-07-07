package com.example.myawesomerecipe
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.client.statement.bodyAsText

class Greeting {
    private val platform = getPlatform()
//    private val client = HttpClient()

    fun greet(): String {
        return sayHello(platform.name)
//        val response = client.get("https://ktor.io/docs/")
//        return response.bodyAsText()
    }

}