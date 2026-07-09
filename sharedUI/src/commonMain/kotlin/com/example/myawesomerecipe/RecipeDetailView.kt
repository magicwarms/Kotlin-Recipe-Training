package com.example.myawesomerecipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myawesomerecipe.model.MealModel

@Composable
fun RecipeDetailView(
    modifier: Modifier = Modifier,
    meal: MealModel,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "< Back",
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (isFavorite) "★" else "☆",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .clickable { onFavoriteClick() }
                    .padding(16.dp)
            )
        }

        AsyncImage(
            model = meal.thumbnail,
            contentDescription = meal.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            meal.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            "${meal.category} - ${meal.country}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Ingredients:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        listOf(
            meal.ingredient1 to meal.measure1,
            meal.ingredient2 to meal.measure2,
            meal.ingredient3 to meal.measure3
        ).forEach { (ingredient, measure) ->
            if (ingredient?.isBlank() == false) {
                val line = if (measure?.isBlank() == false)
                    "$ingredient - $measure"
                else
                    ingredient

                Text(
                    "• $line",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Instructions:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            meal.instructions,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}