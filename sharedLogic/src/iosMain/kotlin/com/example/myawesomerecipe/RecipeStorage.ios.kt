package com.example.myawesomerecipe

import platform.Foundation.NSUserDefaults

actual class RecipeStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val favouritesKey = "favourites"

    actual fun saveFavourite(recipeID: String) {
        val favourites = getFavourites().toMutableList()
        if (!favourites.contains(recipeID)) {
            favourites.add(recipeID)
            userDefaults.setObject(favourites.joinToString(","), favouritesKey)
        }
    }

    actual fun removeFavourite(recipeID: String) {
    }

    actual fun isFavourite(recipeID: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun getFavourites(): List<String> {
        val favourites = userDefaults.stringForKey(favouritesKey) ?: ""
        return favourites.split(",").filter { it.isNotEmpty() }
    }
}