package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yugved4.R
import com.example.yugved4.utils.DietPreferencesManager

/**
 * Diet Result Fragment - Displays calculated calorie goal and user summary
 */
class DietResultFragment : Fragment() {

    private lateinit var tvCaloriesValue: TextView
    private lateinit var tvActivityLevelValue: TextView
    private lateinit var tvDietPrefValue: TextView
    private lateinit var tvAgeGenderValue: TextView
    private lateinit var tvHeightValue: TextView
    private lateinit var tvWeightValue: TextView
    private lateinit var btnGenerateMealPlan: Button
    private lateinit var btnRecalculate: Button
    
    private lateinit var prefsManager: DietPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_diet_result, container, false)
        
        prefsManager = DietPreferencesManager(requireContext())
        
        // Initialize views
        tvCaloriesValue = view.findViewById(R.id.tvCaloriesValue)
        tvActivityLevelValue = view.findViewById(R.id.tvActivityLevelValue)
        tvDietPrefValue = view.findViewById(R.id.tvDietPrefValue)
        tvAgeGenderValue = view.findViewById(R.id.tvAgeGenderValue)
        tvHeightValue = view.findViewById(R.id.tvHeightValue)
        tvWeightValue = view.findViewById(R.id.tvWeightValue)
        btnGenerateMealPlan = view.findViewById(R.id.btnGenerateMealPlan)
        btnRecalculate = view.findViewById(R.id.btnRecalculate)
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadAndDisplayData()
        setupButtons()
    }

    private fun loadAndDisplayData() {
        val dietData = prefsManager.loadDietData()
        
        if (dietData != null) {
            // Display calorie value
            tvCaloriesValue.text = dietData.calculatedCalories.toString()
            
            // Display user details
            tvActivityLevelValue.text = dietData.activityLevel
            tvDietPrefValue.text = dietData.dietPreference
            tvAgeGenderValue.text = "${dietData.age} years, ${dietData.gender}"
            tvHeightValue.text = "${dietData.height} cm"
            tvWeightValue.text = String.format("%.1f kg", dietData.weight)
        }
    }

    private fun setupButtons() {
        btnGenerateMealPlan.setOnClickListener {
            // Placeholder for future meal plan generation
            Toast.makeText(
                requireContext(),
                "Meal plan generation coming soon!",
                Toast.LENGTH_SHORT
            ).show()
        }
        
        btnRecalculate.setOnClickListener {
            // Navigate back to diet survey
            findNavController().popBackStack()
        }
    }
}
