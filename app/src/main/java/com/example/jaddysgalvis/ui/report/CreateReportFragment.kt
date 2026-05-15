package com.example.jaddysgalvis.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jaddysgalvis.R
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.repository.ReportRepository
import com.example.jaddysgalvis.viewmodel.ReportViewModel

class CreateReportFragment : Fragment() {

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_create_report, container, false)

        val database = AppDatabase.getDatabase(requireContext())
        val repository = ReportRepository(database.reportDao())
        viewModel = ReportViewModel(repository)

        val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
        val edtDescription = view.findViewById<EditText>(R.id.edtDescription)

        val btnSave = view.findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {

            val title = edtTitle.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            // VALIDACIONES

            if (title.isEmpty()) {
                edtTitle.error = "El título es obligatorio"
                edtTitle.requestFocus()
                return@setOnClickListener
            }

            if (title.length < 5) {
                edtTitle.error = "Mínimo 5 caracteres"
                edtTitle.requestFocus()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                edtDescription.error = "La descripción es obligatoria"
                edtDescription.requestFocus()
                return@setOnClickListener
            }

            if (description.length < 10) {
                edtDescription.error = "La descripción debe tener mínimo 10 caracteres"
                edtDescription.requestFocus()
                return@setOnClickListener
            }

            val report = ReportEntity(
                title = title,
                description = description,
                category = "General",
                priority = "Alta",
                status = "Pendiente",
                location = "Sin ubicación",
                date = "14/05/2026"
            )

            viewModel.insertReport(report)

            Toast.makeText(
                requireContext(),
                "Reporte creado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            edtTitle.setText("")
            edtDescription.setText("")
        }

        return view
    }
}