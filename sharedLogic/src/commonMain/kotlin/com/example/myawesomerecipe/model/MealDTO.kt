package com.example.myawesomerecipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse (
    @SerialName("meals")
    val meals: List<MealDTO>
)

@Serializable
data class MealDTO (
    @SerialName("idMeal")
    val id: String,
    @SerialName("strMeal")
    val name: String,
    @SerialName("strCategory")
    val category: String,
    @SerialName("strCountry")
    val country: String,
    @SerialName("strInstructions")
    val instructions: String,
    @SerialName("strMealThumb")
    val thumbnail: String,
    @SerialName("strIngredient1")
    val ingredient1: String,
    @SerialName("strMeasure1")
    val measure1: String,
    @SerialName("strIngredient2")
    val ingredient2: String,
    @SerialName("strMeasure2")
    val measure2: String,
    @SerialName("strIngredient3")
    val ingredient3: String,
    @SerialName("strMeasure3")
    val measure3: String
) {
    fun toModel(): MealModel {
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
}
