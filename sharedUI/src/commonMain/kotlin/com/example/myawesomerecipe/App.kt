package com.example.myawesomerecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.presentation.MealViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MealViewModel = koinViewModel()) {
    MaterialTheme {
        var selectedMeal by remember { mutableStateOf<MealModel?>(null) }
        val favoriteIDs by viewModel.favoriteIDs.collectAsState()
        val uiState by viewModel.uiState.collectAsState()

        if (selectedMeal == null) {

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("My Awesome Recipe")

                RecipeListView(
                    uiState = uiState,
                    favoriteIDs = favoriteIDs,
                    onFavoriteClick = {
                        viewModel.toggleFavorite(selectedMeal!!)
                    },
                    onClick = {
                        selectedMeal = it
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            selectedMeal?.let { meal ->
                RecipeDetailView(
                    modifier = Modifier
                        .safeContentPadding()
                        .padding(top = 24.dp),
                    meal = meal,
                    isFavorite = favoriteIDs.contains(meal.id),
                    onFavoriteClick = {
                        viewModel.toggleFavorite(meal)
                    },
                    onBackClick = {
                        selectedMeal = null
                    }
                )
            }
        }
    }
}