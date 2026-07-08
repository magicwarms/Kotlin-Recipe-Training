package com.example.myawesomerecipe.repository

import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.model.MealResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class MealRepositoryImpl : MealRepository {
    private val baseUrl = "https://themealdb.com/api/json/v1/1"
    private val searchUrl = "$baseUrl/search.php?s=chicken"
    private val httpClient = createHttpClient()

    override suspend fun fetchMeals(mealName: String): List<MealModel> {
        val response = httpClient.get("${searchUrl}${mealName}")

        if(response.status == HttpStatusCode.OK){
            val mealResponse = response.body<MealResponse>()

            return mealResponse.meals.map { meal ->
                meal.toModel()
            }
        }

        return listOf()
    }

    override fun favorites(): Flow<List<MealModel>> {
        TODO("Not yet implemented")
    }
}