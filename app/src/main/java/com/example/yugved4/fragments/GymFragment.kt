package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yugved4.R
import com.example.yugved4.adapters.CategoryAdapter
import com.example.yugved4.databinding.FragmentGymBinding
import com.example.yugved4.models.Category

/**
 * Gym Fragment displaying exercise categories in a 2-column grid
 */
class GymFragment : Fragment() {

    private var _binding: FragmentGymBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGymBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCategories()
        setupRecyclerView()
    }

    /**
     * Initialize category data
     */
    private fun setupCategories() {
        categories.apply {
            add(Category(1, "Chest", R.drawable.ic_activity, 12))
            add(Category(2, "Back", R.drawable.ic_activity, 10))
            add(Category(3, "Legs", R.drawable.ic_activity, 15))
            add(Category(4, "Shoulders", R.drawable.ic_activity, 8))
            add(Category(5, "Arms", R.drawable.ic_activity, 14))
            add(Category(6, "Core", R.drawable.ic_activity, 11))
        }
    }

    /**
     * Setup RecyclerView with GridLayoutManager for 2-column layout
     */
    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(categories) { category ->
            // NOTE: GymFragment is deprecated - use WorkoutFragment instead
            // This fragment kept for reference but navigation removed
            // Navigate to ExerciseListFragment with category name using Bundle
            val bundle = Bundle().apply {
                putString("categoryName", category.name)
            }
            // TODO: This navigation no longer works - GymFragment removed from nav_graph
            // findNavController().navigate(R.id.action_gymFragment_to_exerciseListFragment, bundle)
        }

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
