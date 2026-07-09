package com.example.myawesomerecipe.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myawesomerecipe.model.MealModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.myawesomerecipe.model.UiState
import com.example.myawesomerecipe.repository.MealRepository
import com.example.myawesomerecipe.repository.MealRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MealViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val mealRepository: MealRepository = MealRepositoryImpl()
    val favoriteIDs = mealRepository.favorites()
        .map { favorite -> favorite.map {it.id} }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        fetchMeals("meatball")
    }

    fun fetchMeals(mealName: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val meals = mealRepository.fetchMeals(mealName)
                _uiState.value = UiState.Success(meals)
            } catch (ex: Exception) {
                val cachedMeals = mealRepository.favorites().first()
                if (cachedMeals.isNotEmpty()) {
                    _uiState.value = UiState.Success(cachedMeals)
                } else {
                    _uiState.value = UiState.Error(ex.message ?: "Unknown error")
                }
            }
        }
    }

    fun toggleFavorite(meal: MealModel) {
        if(favoriteIDs.value.contains(meal.id)) {
            mealRepository.removeFavorite(meal.id)
        } else {
            mealRepository.saveFavorite(meal)
        }
    }
}
