package com.example.myawesomerecipe.repository

import kotlinx.coroutines.flow.Flow
import com.example.myawesomerecipe.model.MealModel

class MealRepositoryImpl : MealRepository {
    override suspend fun fetchMeals(): List<MealModel> {
        TODO("Not yet implemented")
    }

    override fun favorites(): Flow<List<MealModel>> {
        TODO("Not yet implemented")
    }
}