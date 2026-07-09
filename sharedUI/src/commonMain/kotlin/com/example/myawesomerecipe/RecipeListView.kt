package com.example.myawesomerecipe

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.model.UiState

@Composable
fun RecipeListView(
    uiState: UiState,
    favoriteIDs: Set<String>,
    onFavoriteClick: () -> Unit,
    onClick: (MealModel) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
            Text("Loading...")
        }

        is UiState.Error -> {
            Text("Error: ${uiState.message}")
        }

        is UiState.Success -> {
            val meals = uiState.data

            LazyColumn {
                meals.forEach { meal ->
                    item {
                        RecipeItemView(
                            meal,
                            isFavorite = favoriteIDs.contains(meal.id),
                            onFavoriteClick,
                            onClick
                        )
                    }
                }
            }
        }
    }
}