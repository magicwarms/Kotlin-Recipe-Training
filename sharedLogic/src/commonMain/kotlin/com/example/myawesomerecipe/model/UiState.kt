package com.example.myawesomerecipe.model

interface UiState {
    object Loading: UiState
    data class Success(val data: List<MealModel>): UiState
    data class Error(val message: String): UiState
}