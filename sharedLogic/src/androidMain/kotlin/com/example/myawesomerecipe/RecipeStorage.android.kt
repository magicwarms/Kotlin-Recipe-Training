package com.example.myawesomerecipe

import android.app.Application

actual class RecipeStorage actual constructor() {
    private val prefs = Application().getSharedPreferences("RecipeStorage", 0)

    actual fun saveFavourite(recipeID: String) {
        prefs.edit().putString("favourite", recipeID).apply()
    }

    actual fun removeFavourite(recipeID: String) {
        prefs.edit().remove("favourite").apply()
    }

    actual fun isFavourite(recipeID: String): Boolean {
        return prefs.getString("favourite", null) == recipeID
    }

    actual fun getFavourites(): List<String> {
        return prefs.getString("favourites", null)?.split(",") ?: emptyList()
    }
}