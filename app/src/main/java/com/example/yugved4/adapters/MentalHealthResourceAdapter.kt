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
 * Adapter for Mental Health Resources (Videos and Tips)
 */
class MentalHealthResourceAdapter(
    private val resources: List<DatabaseHelper.MentalHealthContent>
) : RecyclerView.Adapter<MentalHealthResourceAdapter.ResourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mental_health_resource, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        holder.bind(resources[position])
    }

    override fun getItemCount(): Int = resources.size

    inner class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContentData: TextView = itemView.findViewById(R.id.tvContentData)
        private val btnAction: MaterialButton = itemView.findViewById(R.id.btnAction)

        fun bind(resource: DatabaseHelper.MentalHealthContent) {
            // Set type badge
            tvType.text = resource.type

            // Set category
            tvCategory.text = resource.category

            // Set title
            tvTitle.text = resource.title

            // Set content data
            tvContentData.text = resource.contentData

            // Configure button based on type
            when (resource.type) {
                "Video" -> {
                    btnAction.text = "Watch Now"
                    btnAction.setIconResource(R.drawable.ic_play)
                    btnAction.setOnClickListener {
                        // Open video URL
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.contentData))
                            itemView.context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                "Tip" -> {
                    btnAction.text = "Read More"
                    btnAction.icon = null
                    btnAction.setOnClickListener {
                        // Expand the text view to show full content
                        if (tvContentData.maxLines == 3) {
                            tvContentData.maxLines = Int.MAX_VALUE
                            btnAction.text = "Show Less"
                        } else {
                            tvContentData.maxLines = 3
                            btnAction.text = "Read More"
                        }
                    }
                }
            }
        }
    }
}
