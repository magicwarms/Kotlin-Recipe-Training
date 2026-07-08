package com.example.myawesomerecipe.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import com.example.myawesomerecipe.model.UiState
import com.example.myawesomerecipe.repository.MealRepository
import com.example.myawesomerecipe.repository.MealRepositoryImpl

class MealViewModel : ViewModel() {
    val uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val mealRepository: MealRepository = MealRepositoryImpl()

    fun fetchMeals(): UiState {
        uiState.value = UiState.Loading

        try {
            // Successive API calls
        } catch (ex: Exception) {
            uiState.value = UiState.Error(ex.message ?: "Unknown error")
        }

        return UiState.Success(emptyList())
    }
}