package com.example.jaddysgalvis.ui.report.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

            // COLOR STATUS
            binding.txtStatus.setBackgroundColor(
                when (report.status) {
                    "Resuelto" -> Color.parseColor("#16A34A")
                    "En proceso" -> Color.parseColor("#F59E0B")
                    else -> Color.parseColor("#2563EB")
                }
            )

            // COLOR PRIORIDAD
            binding.txtPriority.setBackgroundColor(
                when (report.priority) {
                    "Alta" -> Color.parseColor("#DC2626")
                    "Media" -> Color.parseColor("#F97316")
                    else -> Color.parseColor("#16A34A")
                }
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportViewHolder {

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