package com.example.yugved4.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.yugved4.models.UserDietData

/**
 * Manager for saving and loading diet preferences using SharedPreferences
 */
class DietPreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "diet_preferences",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_AGE = "age"
        private const val KEY_GENDER = "gender"
        private const val KEY_HEIGHT = "height"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_ACTIVITY_LEVEL = "activity_level"
        private const val KEY_DIET_PREFERENCE = "diet_preference"
        private const val KEY_CALCULATED_CALORIES = "calculated_calories"
    }
    
    /**
     * Save user diet data
     */
    fun saveDietData(data: UserDietData) {
        prefs.edit().apply {
            putInt(KEY_AGE, data.age)
            putString(KEY_GENDER, data.gender)
            putInt(KEY_HEIGHT, data.height)
            putFloat(KEY_WEIGHT, data.weight)
            putString(KEY_ACTIVITY_LEVEL, data.activityLevel)
            putString(KEY_DIET_PREFERENCE, data.dietPreference)
            putInt(KEY_CALCULATED_CALORIES, data.calculatedCalories)
            apply()
        }
    }
    
    /**
     * Load saved diet data
     */
    fun loadDietData(): UserDietData? {
        val age = prefs.getInt(KEY_AGE, 0)
        if (age == 0) return null // No data saved
        
        return UserDietData(
            age = age,
            gender = prefs.getString(KEY_GENDER, "") ?: "",
            height = prefs.getInt(KEY_HEIGHT, 0),
            weight = prefs.getFloat(KEY_WEIGHT, 0f),
            activityLevel = prefs.getString(KEY_ACTIVITY_LEVEL, "") ?: "",
            dietPreference = prefs.getString(KEY_DIET_PREFERENCE, "") ?: "",
            calculatedCalories = prefs.getInt(KEY_CALCULATED_CALORIES, 0)
        )
    }
    
    /**
     * Clear all saved diet data
     */
    fun clearDietData() {
        prefs.edit().clear().apply()
    }
}
