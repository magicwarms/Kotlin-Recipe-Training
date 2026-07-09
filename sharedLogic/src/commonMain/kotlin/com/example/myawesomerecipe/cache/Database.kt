package com.example.myawesomerecipe.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myawesomerecipe.model.MealModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class Database(driverFactory: DatabaseDriverFactory) {
    private val database = RecipeDatabase(driverFactory.createDriver())
    private val dbQuery = database.recipeDatabaseQueries

    fun insertMeal(meal: MealModel): Int {
        return dbQuery.insertMeal(
            meal.id, meal.name, meal.category,
            meal.country, meal.instructions, meal.thumbnail,
            meal.ingredient1, meal.measure1,
            meal.ingredient2, meal.measure2,
            meal.ingredient3, meal.measure3
        ).value.toInt()
    }

    fun getMeals(): List<MealModel> {
        return dbQuery
            .selectAllMealsInfo()
            .executeAsList()
            .map { it.toModel() }
    }

    fun getMealsAsFlow(): Flow<List<MealModel>> {
        return dbQuery
            .selectAllMealsInfo()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { meals ->
                meals.map { it.toModel() }
            }
    }

    fun removeMeal(mealID: String): Int {
        return dbQuery.removeMeal(mealID).value.toInt()
    }
}

private fun Meal.toModel(): MealModel {
    return MealModel(
        id,
        name,
        category,
        country,
        instructions,
        thumbnail,
        ingredient1,
        measure1,
        ingredient2,
        measure2,
        ingredient3,
        measure3
    )
}