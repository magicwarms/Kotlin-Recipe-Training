package com.example.myawesomerecipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myawesomerecipe.model.MealModel

@Composable
fun RecipeItemView(
    meal: MealModel,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: (MealModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(meal) },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
                .copy(alpha = 0.8f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                .copy(alpha = 0.8f),
            disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
            disabledContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    ) {
        Row {
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = meal.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
            ) {
                Text(
                    meal.name,
                    style = MaterialTheme.typography.titleMedium
                )

                meal.country?.let { country ->
                    Text(country)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (isFavorite) "★" else "☆",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .clickable { onFavoriteClick() }
                    .padding(16.dp)
            )
        }
    }
}
