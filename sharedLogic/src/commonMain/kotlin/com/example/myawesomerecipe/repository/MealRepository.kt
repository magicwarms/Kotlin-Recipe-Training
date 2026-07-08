package com.example.myawesomerecipe.repository

import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.model.MealModel

interface MealRepository {
    suspend fun fetchMeals(): List<MealModel>
    fun favorites(): Flow<List<MealModel>>
}