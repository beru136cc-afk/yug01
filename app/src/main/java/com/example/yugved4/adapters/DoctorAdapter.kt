package com.example.yugved4.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yugved4.R
import com.example.yugved4.models.Doctor
import com.google.android.material.button.MaterialButton

/**
 * Adapter for displaying doctors in RecyclerView
 */
class DoctorAdapter(
    private val doctors: MutableList<Doctor>,
    private val onCallClick: (Doctor) -> Unit,
    private val onWhatsAppClick: (Doctor) -> Unit,
    private val onDeleteClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvSpecialty: TextView = itemView.findViewById(R.id.tvSpecialty)
        val tvPhoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val btnCall: MaterialButton = itemView.findViewById(R.id.btnCall)
        val btnWhatsApp: MaterialButton = itemView.findViewById(R.id.btnWhatsApp)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        
        holder.tvDoctorName.text = doctor.name
        holder.tvSpecialty.text = doctor.specialty
        holder.tvPhoneNumber.text = doctor.phoneNumber
        
        // Call button
        holder.btnCall.setOnClickListener {
            onCallClick(doctor)
        }
        
        // WhatsApp button
        holder.btnWhatsApp.setOnClickListener {
            onWhatsAppClick(doctor)
        }
        
        // Delete button
        holder.btnDelete.setOnClickListener {
            onDeleteClick(doctor)
        }
    }

    override fun getItemCount(): Int = doctors.size
    
    /**
     * Remove doctor from list
     */
    fun removeDoctor(doctor: Doctor) {
        val position = doctors.indexOf(doctor)
        if (position != -1) {
            doctors.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    /**
     * Add doctor to list
     */
    fun addDoctor(doctor: Doctor) {
        doctors.add(doctor)
        notifyItemInserted(doctors.size - 1)
    }
}
