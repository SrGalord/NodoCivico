package com.example.jaddysgalvis.ui.report.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.entity.ReportEntity

class ReportAdapter(

    private var reports: List<ReportEntity>,

    private val onDetailClick: (ReportEntity) -> Unit,

    private val onEditClick: (ReportEntity) -> Unit,

    private val onDeleteClick: (ReportEntity) -> Unit

) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtTitle: TextView =
            itemView.findViewById(R.id.txtTitle)

        val txtDescription: TextView =
            itemView.findViewById(R.id.txtDescription)

        val txtStatus: TextView =
            itemView.findViewById(R.id.txtStatus)

        val btnEdit: Button =
            itemView.findViewById(R.id.btnEdit)

        val btnDelete: Button =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)

        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ReportViewHolder,
        position: Int
    ) {

        val report = reports[position]

        holder.txtTitle.text = report.title
        holder.txtDescription.text = report.description
        holder.txtStatus.text = report.status

        when (report.status) {

            "Pendiente" -> {
                holder.txtStatus.setTextColor(Color.RED)
            }

            "En proceso" -> {
                holder.txtStatus.setTextColor(
                    Color.parseColor("#FFA000")
                )
            }

            "Resuelto" -> {
                holder.txtStatus.setTextColor(Color.GREEN)
            }

            else -> {
                holder.txtStatus.setTextColor(Color.GRAY)
            }
        }

        // DETALLE
        holder.itemView.setOnClickListener {
            onDetailClick(report)
        }

        // EDITAR
        holder.btnEdit.setOnClickListener {
            onEditClick(report)
        }

        // ELIMINAR
        holder.btnDelete.setOnClickListener {
            onDeleteClick(report)
        }
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    fun updateData(newReports: List<ReportEntity>) {

        reports = newReports

        notifyDataSetChanged()
    }
}