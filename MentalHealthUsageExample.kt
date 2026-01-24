package com.example.yugved4.examples

import android.content.Context
import com.example.yugved4.database.DatabaseHelper

/**
 * Example usage of Mental Health Module functions
 * 
 * This file demonstrates how to use the Mental Health Content database methods
 * in your MentalHealthFragment or any other activity/fragment.
 */

class MentalHealthUsageExample(context: Context) {
    
    private val dbHelper = DatabaseHelper(context)
    
    /**
     * Example 1: Get all meditation content
     */
    fun getMeditationContent() {
        val meditationContent = dbHelper.getMentalHealthContent("Meditation")
        
        // Process the results
        meditationContent.forEach { content ->
            println("ID: ${content.id}")
            println("Title: ${content.title}")
            println("Type: ${content.type}")
            println("Category: ${content.category}")
            println("Content Data: ${content.contentData}")
            println("---")
        }
    }
    
    /**
     * Example 2: Get all stress-related content
     */
    fun getStressContent() {
        val stressContent = dbHelper.getMentalHealthContent("Stress")
        
        // Use in RecyclerView adapter
        // adapter.submitList(stressContent)
    }
    
    /**
     * Example 3: Get all sleep content
     */
    fun getSleepContent() {
        val sleepContent = dbHelper.getMentalHealthContent("Sleep")
        
        // Display in UI
        sleepContent.forEach { content ->
            if (content.type == "Video") {
                // Open video URL: content.contentData
            } else {
                // Display tip text: content.contentData
            }
        }
    }
    
    /**
     * Example 4: Get all mental health content (unfiltered)
     */
    fun getAllContent() {
        val allContent = dbHelper.getAllMentalHealthContent()
        
        println("Total mental health records: ${allContent.size}")
    }
    
    /**
     * Example 5: Use in MentalHealthFragment
     */
    fun loadContentByCategory(category: String) {
        // Get content from database
        val content = dbHelper.getMentalHealthContent(category)
        
        // Filter by type if needed
        val videos = content.filter { it.type == "Video" }
        val tips = content.filter { it.type == "Tip" }
        
        // Update UI
        // videoRecyclerView.adapter = VideoAdapter(videos)
        // tipRecyclerView.adapter = TipAdapter(tips)
    }
}

/**
 * Example 6: Using in MentalHealthFragment.kt
 * 
 * Add this code to your MentalHealthFragment:
 * 
 * private lateinit var dbHelper: DatabaseHelper
 * 
 * override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *     super.onViewCreated(view, savedInstanceState)
 *     
 *     // Initialize database helper
 *     dbHelper = DatabaseHelper(requireContext())
 *     
 *     // Load meditation content when user clicks "Meditation" button
 *     btnMeditation.setOnClickListener {
 *         val meditationContent = dbHelper.getMentalHealthContent("Meditation")
 *         displayContent(meditationContent)
 *     }
 *     
 *     // Load stress content when user clicks "Stress" button
 *     btnStress.setOnClickListener {
 *         val stressContent = dbHelper.getMentalHealthContent("Stress")
 *         displayContent(stressContent)
 *     }
 *     
 *     // Load sleep content when user clicks "Sleep" button
 *     btnSleep.setOnClickListener {
 *         val sleepContent = dbHelper.getMentalHealthContent("Sleep")
 *         displayContent(sleepContent)
 *     }
 * }
 * 
 * private fun displayContent(contentList: List<DatabaseHelper.MentalHealthContent>) {
 *     contentList.forEach { content ->
 *         when (content.type) {
 *             "Video" -> {
 *                 // Open YouTube or video URL
 *                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content.contentData))
 *                 startActivity(intent)
 *             }
 *             "Tip" -> {
 *                 // Display tip in a TextView or Dialog
 *                 Toast.makeText(context, content.contentData, Toast.LENGTH_LONG).show()
 *             }
 *         }
 *     }
 * }
 */
