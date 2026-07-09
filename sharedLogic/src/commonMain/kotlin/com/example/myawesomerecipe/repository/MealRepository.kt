package com.example.myawesomerecipe.repository

import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.model.MealModel

interface MealRepository {
    suspend fun fetchMeals(mealName: String): List<MealModel>
    fun favorites(): Flow<List<MealModel>>
    fun saveFavorite(meal: MealModel): Int
    fun removeFavorite(mealID: String): Int
}
