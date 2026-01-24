package com.example.yugved4.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yugved4.R
import com.example.yugved4.adapters.HelplineAdapter
import com.example.yugved4.models.Helpline
import com.example.yugved4.utils.HelplineDataProvider

/**
 * Emergency Tab Fragment - Displays emergency helplines with one-tap calling
 */
class EmergencyTabFragment : Fragment() {

    private lateinit var rvEmergency: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.tab_emergency, container, false)

        // Initialize RecyclerView
        rvEmergency = view.findViewById(R.id.rvEmergency)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        // Get helplines data
        val helplines = HelplineDataProvider.getHelplines()

        // Create adapter with click listener for one-tap calling
        val adapter = HelplineAdapter(helplines) { helpline ->
            initiateCall(helpline)
        }

        // Setup RecyclerView
        rvEmergency.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)
        }
    }

    /**
     * Initiates a phone call using Intent.ACTION_DIAL
     * This opens the phone dialer with the number pre-filled
     */
    private fun initiateCall(helpline: Helpline) {
        try {
            val phoneUri = Uri.parse("tel:${helpline.phoneNumber}")
            val dialIntent = Intent(Intent.ACTION_DIAL, phoneUri)

            // Check if there's an app that can handle this intent
            if (dialIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(dialIntent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No phone app found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Unable to open dialer: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
