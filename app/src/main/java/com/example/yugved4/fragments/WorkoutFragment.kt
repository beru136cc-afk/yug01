package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yugved4.R
import com.google.android.material.card.MaterialCardView

/**
 * Workout Hub Fragment
 * Provides unified entry point for both Gym and Yoga workouts
 */
class WorkoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupGymCard(view)
        setupYogaCard(view)
    }
    
    /**
     * Setup Gym card with navigation to gym categories
     */
    private fun setupGymCard(view: View) {
        val cardGym = view.findViewById<MaterialCardView>(R.id.cardGym)
        val btnExploreGym = view.findViewById<View>(R.id.btnExploreGym)
        
        // Both card and button navigate to gym categories
        val gymClickListener = View.OnClickListener {
            navigateToGymCategories()
        }
        
        cardGym.setOnClickListener(gymClickListener)
        btnExploreGym.setOnClickListener(gymClickListener)
    }
    
    /**
     * Setup Yoga card with navigation to yoga asanas
     */
    private fun setupYogaCard(view: View) {
        val cardYoga = view.findViewById<MaterialCardView>(R.id.cardYoga)
        val btnExploreYoga = view.findViewById<View>(R.id.btnExploreYoga)
        
        // Both card and button navigate to yoga asanas
        val yogaClickListener = View.OnClickListener {
            navigateToYogaAsanas()
        }
        
        cardYoga.setOnClickListener(yogaClickListener)
        btnExploreYoga.setOnClickListener(yogaClickListener)
    }
    
    /**
     * Navigate to Gym categories (shows GymFragment content)
     */
    private fun navigateToGymCategories() {
        try {
            // Navigate to exercise list with "Gym" category using Bundle
            val bundle = Bundle().apply {
                putString("categoryName", "Gym")
            }
            findNavController().navigate(
                R.id.action_workoutFragment_to_exerciseListFragment,
                bundle
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Navigate to Yoga asanas (shows YogaFragment content)
     */
    private fun navigateToYogaAsanas() {
        try {
            // Navigate to asana list with "Yoga" category using Bundle
            val bundle = Bundle().apply {
                putString("categoryName", "Yoga")
            }
            findNavController().navigate(
                R.id.action_workoutFragment_to_asanaListFragment,
                bundle
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
