package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.yugved4.R
import com.example.yugved4.database.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Profile Fragment for viewing and editing user profile
 */
class ProfileFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    
    // Editable fields
    private lateinit var etName: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    
    // Read-only fields
    private lateinit var tvGenderValue: TextView
    private lateinit var tvActivityLevelValue: TextView
    private lateinit var tvDietPreferenceValue: TextView
    private lateinit var tvTargetCaloriesValue: TextView
    
    // Save button
    private lateinit var btnSaveProfile: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        databaseHelper = DatabaseHelper(requireContext())
        
        // Initialize views
        initializeViews(view)
        
        // Load profile data
        loadUserProfile()
        
        // Set up save button
        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
        
        return view
    }
    
    private fun initializeViews(view: View) {
        // Editable fields
        etName = view.findViewById(R.id.etName)
        etAge = view.findViewById(R.id.etAge)
        etHeight = view.findViewById(R.id.etHeight)
        etWeight = view.findViewById(R.id.etWeight)
        
        // Read-only fields
        tvGenderValue = view.findViewById(R.id.tvGenderValue)
        tvActivityLevelValue = view.findViewById(R.id.tvActivityLevelValue)
        tvDietPreferenceValue = view.findViewById(R.id.tvDietPreferenceValue)
        tvTargetCaloriesValue = view.findViewById(R.id.tvTargetCaloriesValue)
        
        // Button
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
    }
    
    /**
     * Load user profile from database and populate fields
     */
    private fun loadUserProfile() {
        val profile = databaseHelper.getUserProfile()
        
        if (profile != null) {
            // Populate editable fields
            etName.setText(profile.name ?: "")
            etAge.setText(profile.age?.toString() ?: "")
            etHeight.setText(profile.height?.toString() ?: "")
            etWeight.setText(profile.currentWeight.toString())
            
            // Populate read-only fields
            tvGenderValue.text = profile.gender ?: "-"
            tvActivityLevelValue.text = profile.activityLevel ?: "-"
            tvDietPreferenceValue.text = profile.dietPreference ?: "-"
            tvTargetCaloriesValue.text = "${profile.targetCalories} kcal"
        } else {
            Toast.makeText(
                requireContext(),
                "No profile found. Please complete the diet survey first.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    /**
     * Validate user inputs
     */
    private fun validateInputs(): Boolean {
        val ageText = etAge.text.toString()
        val heightText = etHeight.text.toString()
        val weightText = etWeight.text.toString()
        
        when {
            ageText.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your age", Toast.LENGTH_SHORT).show()
                etAge.requestFocus()
                return false
            }
            heightText.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your height", Toast.LENGTH_SHORT).show()
                etHeight.requestFocus()
                return false
            }
            weightText.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your weight", Toast.LENGTH_SHORT).show()
                etWeight.requestFocus()
                return false
            }
            else -> {
                val age = ageText.toIntOrNull() ?: 0
                val height = heightText.toDoubleOrNull() ?: 0.0
                val weight = weightText.toDoubleOrNull() ?: 0.0
                
                when {
                    age < 10 || age > 120 -> {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid age (10-120)",
                            Toast.LENGTH_SHORT
                        ).show()
                        etAge.requestFocus()
                        return false
                    }
                    height < 100 || height > 250 -> {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid height (100-250 cm)",
                            Toast.LENGTH_SHORT
                        ).show()
                        etHeight.requestFocus()
                        return false
                    }
                    weight < 30 || weight > 200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid weight (30-200 kg)",
                            Toast.LENGTH_SHORT
                        ).show()
                        etWeight.requestFocus()
                        return false
                    }
                }
                return true
            }
        }
    }
    
    /**
     * Save profile changes to database and recalculate BMR
     */
    private fun saveProfile() {
        if (!validateInputs()) {
            return
        }
        
        val name = etName.text.toString().trim().ifEmpty { null }
        val age = etAge.text.toString().toInt()
        val height = etHeight.text.toString().toDouble()
        val weight = etWeight.text.toString().toDouble()
        
        try {
            // Update profile in database
            val rowsAffected = databaseHelper.updateUserProfile(name, age, height, weight)
            
            if (rowsAffected > 0) {
                // Recalculate and update target calories
                databaseHelper.updateTargetCalories()
                
                // Reload profile to show updated values
                loadUserProfile()
                
                Toast.makeText(
                    requireContext(),
                    "Profile updated successfully! Target calories recalculated.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to update profile. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}
