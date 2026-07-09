package com.example.myawesomerecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myawesomerecipe.model.UiState
import com.example.myawesomerecipe.presentation.MealViewModel

@Composable
fun App(viewModel: MealViewModel = MealViewModel()) {
    MaterialTheme {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text("My Awesome Recipes")
                    }
                )
            }
        ) { innerPadding ->
            val favoriteIDs by viewModel.favoriteIDs.collectAsState()
            val uiState by viewModel.uiState.collectAsState()

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (uiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                        Text("Loading...")
                    }

                    is UiState.Error -> {
                        Text("Error: ${(uiState as UiState.Error).message}")
                    }

                    is UiState.Success -> {
                        val meals = (uiState as UiState.Success).data
                        LazyColumn {
                            meals.forEach { meal ->
                                item {
                                    RecipeItemView(
                                        meal,
                                        isFavorite = favoriteIDs.contains(meal.id),
                                        onFavoriteClick = { viewModel.toggleFavorite(meal) },
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
