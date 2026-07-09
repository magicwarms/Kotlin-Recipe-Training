package com.example.myawesomerecipe.repository

import com.example.myawesomerecipe.cache.Database
import com.example.myawesomerecipe.cache.createDriverFactory
import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.model.MealResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class MealRepositoryImpl : MealRepository {
    private val baseUrl = "https://themealdb.com/api/json/v1/1"
    private val searchUrl = "$baseUrl/search.php?s="
    private val httpClient = createHttpClient()
    private val database = Database(createDriverFactory())

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
        return database.getMealsAsFlow()
    }

    override fun saveFavorite(meal: MealModel): Int {
        return database.insertMeal(meal)
    }

    override fun removeFavorite(mealID: String): Int {
        return database.removeMeal(mealID)
    }
}