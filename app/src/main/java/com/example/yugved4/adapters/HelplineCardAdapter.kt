package com.example.yugved4.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yugved4.R
import com.example.yugved4.database.DatabaseHelper
import com.google.android.material.button.MaterialButton

/**
 * Adapter for displaying helplines with one-tap calling
 */
class HelplineCardAdapter(
    private val helplines: List<DatabaseHelper.HelplineData>
) : RecyclerView.Adapter<HelplineCardAdapter.HelplineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelplineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_helpline_card, parent, false)
        return HelplineViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelplineViewHolder, position: Int) {
        holder.bind(helplines[position])
    }

    override fun getItemCount(): Int = helplines.size

    inner class HelplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHelplineName: TextView = itemView.findViewById(R.id.tvHelplineName)
        private val tvHelplineDescription: TextView = itemView.findViewById(R.id.tvHelplineDescription)
        private val tvHelplineNumber: TextView = itemView.findViewById(R.id.tvHelplineNumber)
        private val btnCallNow: MaterialButton = itemView.findViewById(R.id.btnCallNow)

        fun bind(helpline: DatabaseHelper.HelplineData) {
            // Set helpline details
            tvHelplineName.text = helpline.name
            tvHelplineDescription.text = helpline.description
            tvHelplineNumber.text = helpline.number

            // Set up Call Now button with Intent.ACTION_DIAL
            btnCallNow.setOnClickListener {
                try {
                    val phoneUri = Uri.parse("tel:${helpline.number}")
                    val dialIntent = Intent(Intent.ACTION_DIAL, phoneUri)
                    itemView.context.startActivity(dialIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
