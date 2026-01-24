package com.example.yugved4.models

/**
 * Data class for user diet information
 */
data class UserDietData(
    val age: Int = 0,
    val gender: String = "", // "Male", "Female"
    val height: Int = 0, // cm
    val weight: Float = 0f, // kg
    val activityLevel: String = "",
    val dietPreference: String = "", // "Vegetarian", "Non-Veg"
    val calculatedCalories: Int = 0
)
