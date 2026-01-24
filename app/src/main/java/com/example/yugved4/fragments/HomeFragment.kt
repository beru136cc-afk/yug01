package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yugved4.R
import com.example.yugved4.adapters.WorkoutAdapter
import com.example.yugved4.database.DatabaseHelper
import com.example.yugved4.databinding.FragmentHomeBinding
import com.example.yugved4.models.Workout

/**
 * Home Dashboard Fragment
 * Displays user's fitness overview, workouts, quick actions, and mindfulness section
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val workouts = mutableListOf<Workout>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        
        checkAndLoadUserProfile()
        setupWorkouts()
        setupQuickActions()
        setupMindfulness()
    }
    
    /**
     * Check if user profile exists, load data or navigate to Diet
     */
    private fun checkAndLoadUserProfile() {
        try {
            // Check if database has user profile
            val hasProfile = dbHelper.hasUserProfile()
            
            if (!hasProfile) {
                // Database empty → Show default UI and optionally navigate to Diet
                showEmptyProfileUI()
                
                Toast.makeText(
                    requireContext(),
                    "Please complete your diet profile",
                    Toast.LENGTH_LONG
                ).show()
                
                // Safe navigation to Diet tab (only if fragment is attached and navigation is available)
                try {
                    if (isAdded && view != null) {
                        findNavController().navigate(R.id.dietFragment)
                    }
                } catch (navException: Exception) {
                    // Navigation failed - user can manually navigate via bottom nav
                    navException.printStackTrace()
                }
                return
            }
            
            // Load and display user data
            loadUserData()
        } catch (e: Exception) {
            // Database error - show safe default UI
            e.printStackTrace()
            showEmptyProfileUI()
            Toast.makeText(
                requireContext(),
                "Error loading profile: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    /**
     * Fetch user profile from SQL and update UI
     */
    private fun loadUserData() {
        try {
            val profile = dbHelper.getUserProfile()
            
            profile?.let {
                // Update Welcome Message
                val userName = if (!it.name.isNullOrEmpty()) it.name else "there"
                binding.tvWelcome.text = "Welcome back, $userName!"
                
                // Update Target Calories with safe handling
                val targetCals = it.targetCalories
                binding.tvCaloriesGoal.text = "$targetCals"
                
                // Current calories (dummy for now - could track throughout the day)
                val currentCalories = 0  // Default to 0 for new day
                binding.tvCaloriesCurrent.text = "$currentCalories"
                
                // Update Circular Progress Bar with safe division
                val progress = if (targetCals > 0) {
                    (currentCalories.toFloat() / targetCals * 100).toInt()
                } else {
                    0
                }
                binding.circularProgress.progress = progress
                
            } ?: run {
                // No profile data - show empty state
                showEmptyProfileUI()
            }
        } catch (e: Exception) {
            // SQL query failed - show safe default
            e.printStackTrace()
            showEmptyProfileUI()
            Toast.makeText(
                requireContext(),
                "Database error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    /**
     * Show empty profile UI with safe defaults
     */
    private fun showEmptyProfileUI() {
        try {
            binding.tvWelcome.text = "Welcome to Yugved!"
            binding.tvCaloriesGoal.text = "0"
            binding.tvCaloriesCurrent.text = "0"
            binding.circularProgress.progress = 0
        } catch (e: Exception) {
            // Even this failed - log but don't crash
            e.printStackTrace()
        }
    }

    /**
     * Setup workouts RecyclerView with dummy data
     */
    private fun setupWorkouts() {
        // Create dummy workout data
        workouts.apply {
            add(
                Workout(
                    id = 1,
                    title = "Morning Yoga",
                    duration = "20 min",
                    imageResId = R.drawable.ic_activity, // Using placeholder icon
                    category = "Yoga"
                )
            )
            add(
                Workout(
                    id = 2,
                    title = "HIIT Cardio",
                    duration = "15 min",
                    imageResId = R.drawable.ic_activity, // Using placeholder icon
                    category = "Cardio"
                )
            )
        }

        // Setup RecyclerView
        workoutAdapter = WorkoutAdapter(workouts)
        binding.rvWorkouts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = workoutAdapter
        }
    }

    /**
     * Setup quick action chip click listeners
     */
    private fun setupQuickActions() {
        binding.chipWater.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Log Water clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.chipMeals.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Log Meals clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.chipActivity.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Log Activity clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Setup mindfulness card button listener
     */
    private fun setupMindfulness() {
        binding.btnStartMindfulness.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Start Mindfulness clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
