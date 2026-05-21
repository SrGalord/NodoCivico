package com.example.jaddysgalvis.ui.report.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.databinding.ItemReportBinding

class ReportAdapter(
    private val onDetailClick: (ReportEntity) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reports: MutableList<ReportEntity> = mutableListOf()

    inner class ReportViewHolder(
        private val binding: ItemReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(report: ReportEntity) {

            binding.txtTitle.text = report.title
            binding.txtDescription.text = report.description
            binding.txtStatus.text = report.status
            binding.txtPriority.text = report.priority

            // CLICK
            binding.root.setOnClickListener {
                onDetailClick(report)
            }

            // STATUS COLOR
            val status = report.status.lowercase()

            val statusColor = when (status) {
                "resuelto" -> "#16A34A"
                "en proceso" -> "#F59E0B"
                "pendiente" -> "#2563EB"
                else -> "#64748B"
            }

            binding.txtStatus.setTextColor(Color.parseColor(statusColor))

            // PRIORITY COLOR
            val priority = report.priority.lowercase()

            val priorityColor = when (priority) {
                "alta" -> "#DC2626"
                "media" -> "#F97316"
                "baja" -> "#16A34A"
                else -> "#64748B"
            }

            binding.txtPriority.setTextColor(Color.parseColor(priorityColor))

            // ANIMATION
            val animation = AnimationUtils.loadAnimation(
                binding.root.context,
                R.anim.item_fade_slide
            )

            binding.root.startAnimation(animation)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {

        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    fun updateData(newList: List<ReportEntity>) {
        reports.clear()
        reports.addAll(newList)
        notifyDataSetChanged()
    }
}