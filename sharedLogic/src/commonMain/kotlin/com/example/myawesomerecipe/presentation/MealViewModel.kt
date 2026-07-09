package com.example.myawesomerecipe.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.myawesomerecipe.model.MealModel
import com.example.myawesomerecipe.model.UiState
import com.example.myawesomerecipe.repository.MealRepository
import com.example.myawesomerecipe.repository.MealRepositoryImpl

class MealViewModel(private val mealRepository: MealRepository) : ViewModel() {
    val uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val favoriteIDs = mealRepository.favorites()
        .map { favorites -> favorites.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init {
        fetchMeals()
    }

    fun fetchMeals() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val meals = mealRepository.fetchMeals("chicken")
                uiState.value = UiState.Success(meals)
            } catch (ex: Exception) {
                val cachedMeals = mealRepository.favorites().first()

                if (cachedMeals.isNotEmpty()) {
                    uiState.value = UiState.Success(cachedMeals)
                } else {
                    uiState.value = UiState.Error(ex.message ?: "Unknown error")
                }
            }
        }
    }

    fun toggleFavorite(meal: MealModel) {
        if (favoriteIDs.value.contains(meal.id)) {
            mealRepository.removeFavorite(meal.id)
        } else {
            mealRepository.saveFavorite(meal)
        }
    }
}