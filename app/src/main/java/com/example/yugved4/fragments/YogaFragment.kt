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
import com.example.yugved4.databinding.FragmentYogaBinding
import com.example.yugved4.models.Category
import com.example.yugved4.utils.YogaDataProvider

/**
 * Yoga Fragment displaying yoga categories in a 2-column grid
 */
class YogaFragment : Fragment() {

    private var _binding: FragmentYogaBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYogaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCategories()
        setupRecyclerView()
    }

    /**
     * Initialize yoga category data
     */
    private fun setupCategories() {
        categories.clear()
        categories.addAll(YogaDataProvider.getYogaCategories())
    }

    /**
     * Setup RecyclerView with GridLayoutManager for 2-column layout
     */
    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(categories) { category ->
            // NOTE: YogaFragment is deprecated - use WorkoutFragment instead
            // This fragment kept for reference but navigation removed
            // Navigate to AsanaListFragment with category name
            val bundle = Bundle().apply {
                putString("categoryName", category.name)
            }
            // TODO: This navigation no longer works - YogaFragment removed from nav_graph
            // findNavController().navigate(R.id.action_yogaFragment_to_asanaListFragment, bundle)
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
