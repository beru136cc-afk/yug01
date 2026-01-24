package com.example.yugved4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yugved4.R
import com.example.yugved4.adapters.MentalHealthResourceAdapter
import com.example.yugved4.database.DatabaseHelper

/**
 * Resources Tab Fragment - Displays mental health resources from database
 */
class ResourcesTabFragment : Fragment() {

    private lateinit var rvResources: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.tab_resources, container, false)

        // Initialize database helper
        dbHelper = DatabaseHelper(requireContext())

        // Initialize RecyclerView
        rvResources = view.findViewById(R.id.rvResources)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        // Get all mental health content from database
        val resources = dbHelper.getAllMentalHealthContent()

        // Create adapter
        val adapter = MentalHealthResourceAdapter(resources)

        // Setup RecyclerView
        rvResources.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)
        }
    }
}
