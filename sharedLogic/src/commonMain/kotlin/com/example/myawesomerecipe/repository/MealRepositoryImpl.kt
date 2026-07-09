package com.example.myawesomerecipe.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.cache.Database
import com.example.myawesomerecipe.cache.createDriverFactory
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.model.MealResponse

class MealRepositoryImpl : MealRepository {
    private val baseURL = "https://themealdb.com/api/json/v1/1"
    private val searchURL = "$baseURL/search.php?s="
    private val httpClient = createHttpClient()
    private val database = Database(createDriverFactory())

    override suspend fun fetchMeals(mealName: String): List<MealModel> {
        val response = httpClient.get("${searchURL}${mealName}")

        if (response.status == HttpStatusCode.OK) {
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
        val insertedCount = database.insertMeal(meal)
        return insertedCount
    }

    override fun removeFavorite(mealID: String): Int {
        val deletedCount = database.removeMeal(mealID)
        return deletedCount
    }
}