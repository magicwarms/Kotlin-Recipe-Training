package com.example.myawesomerecipe.cache

internal class Database(driverFactory: DatabaseDriverFactory) {
    private val database = RecipeDatabase(driverFactory.createDriver())
    private val dbQuery = database.recipeDatabaseQueries

    fun getMeals(): List<Meal> {
        return dbQuery.selectAllMealsInfo().executeAsList()
    }
}