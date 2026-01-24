package com.example.yugved4.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yugved4.R
import com.example.yugved4.adapters.DoctorAdapter
import com.example.yugved4.database.DatabaseHelper
import com.example.yugved4.models.Doctor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Doctor Fragment - Manage doctor directory with SQLite Database
 */
class DoctorFragment : Fragment() {

    private lateinit var rvDoctors: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var fabAddDoctor: FloatingActionButton
    
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val doctors = mutableListOf<Doctor>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        
        dbHelper = DatabaseHelper(requireContext())
        
        rvDoctors = view.findViewById(R.id.rvDoctors)
        emptyState = view.findViewById(R.id.emptyState)
        fabAddDoctor = view.findViewById(R.id.fabAddDoctor)
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadDoctors()
        setupFAB()
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        doctorAdapter = DoctorAdapter(
            doctors,
            onCallClick = { doctor -> initiateCall(doctor.phoneNumber) },
            onWhatsAppClick = { doctor -> openWhatsApp(doctor.phoneNumber) },
            onDeleteClick = { doctor -> confirmDeleteDoctor(doctor) }
        )
        
        rvDoctors.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = doctorAdapter
        }
    }

    private fun loadDoctors() {
        doctors.clear()
        doctors.addAll(dbHelper.getAllDoctors())
    }

    private fun setupFAB() {
        fabAddDoctor.setOnClickListener {
            showAddDoctorDialog()
        }
    }

    private fun updateEmptyState() {
        if (doctors.isEmpty()) {
            rvDoctors.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            rvDoctors.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    private fun showAddDoctorDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_doctor, null)
        
        val etDoctorName = dialogView.findViewById<TextInputEditText>(R.id.etDoctorName)
        val etSpecialty = dialogView.findViewById<TextInputEditText>(R.id.etSpecialty)
        val etPhoneNumber = dialogView.findViewById<TextInputEditText>(R.id.etPhoneNumber)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        
        dialogView.findViewById<View>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }
        
        dialogView.findViewById<View>(R.id.btnSave).setOnClickListener {
            val name = etDoctorName.text.toString().trim()
            val specialty = etSpecialty.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()
            
            if (validateInput(name, specialty, phoneNumber)) {
                addDoctor(name, specialty, phoneNumber)
                dialog.dismiss()
            }
        }
        
        dialog.show()
    }

    private fun validateInput(name: String, specialty: String, phoneNumber: String): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show()
                false
            }
            specialty.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.please_enter_specialty), Toast.LENGTH_SHORT).show()
                false
            }
            phoneNumber.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.please_enter_phone), Toast.LENGTH_SHORT).show()
                false
            }
            phoneNumber.length < 10 -> {
                Toast.makeText(requireContext(), getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun addDoctor(name: String, specialty: String, phoneNumber: String) {
        val id = dbHelper.addDoctor(name, specialty, phoneNumber)
        
        if (id > 0) {
            // Reload doctors from database
            loadDoctors()
            doctorAdapter.notifyDataSetChanged()
            updateEmptyState()
            Toast.makeText(requireContext(), getString(R.string.doctor_added), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to add doctor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDeleteDoctor(doctor: Doctor) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.delete_doctor_confirm))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteDoctor(doctor)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteDoctor(doctor: Doctor) {
        val rowsDeleted = dbHelper.deleteDoctor(doctor.id)
        
        if (rowsDeleted > 0) {
            loadDoctors()
            doctorAdapter.notifyDataSetChanged()
            updateEmptyState()
            Toast.makeText(requireContext(), getString(R.string.doctor_deleted), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to delete doctor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initiateCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun openWhatsApp(phoneNumber: String) {
        try {
            // Remove any non-digit characters from phone number
            val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$cleanNumber")
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.whatsapp_not_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Note: DatabaseHelper doesn't need to be closed here
        // as it's designed to be a singleton
    }
}
