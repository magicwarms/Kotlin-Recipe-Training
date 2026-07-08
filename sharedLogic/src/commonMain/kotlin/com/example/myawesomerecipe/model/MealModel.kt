package com.example.myawesomerecipe.model

data class MealModel (
    val id: String,
    val name: String,
    val category: String,
    val country: String,
    val instructions: String,
    val thumbnail: String,
    val ingredient1: String,
    val measure1: String,
    val ingredient2:String,
    val measure2: String,
    val ingredient3: String,
    val measure3: String,
)