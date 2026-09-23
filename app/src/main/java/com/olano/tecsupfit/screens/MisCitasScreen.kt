package com.olano.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olano.navlab.model.Cita

@Composable
fun MisCitasScreen() {
    val listaCitas = listOf(
        Cita("Dra. Ana Torres", "Viernes 27, 10:30 am", "Confirmada", "Confirmada"),
        Cita("Dr. Luis Vega", "Miércoles 15, 3:00 pm", "Completada", "Completada")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis citas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listaCitas) { cita ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(cita.doctorNombre, style = MaterialTheme.typography.titleMedium)
                        Text(cita.fecha)
                        Spacer(modifier = Modifier.height(8.dp))
                        AssistChip(
                            onClick = {},
                            label = { Text(cita.estado) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Perfil del Paciente", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre: Brayan Olano", style = MaterialTheme.typography.titleMedium)
        Text("Correo: brayan.olano@tecsup.edu.pe", style = MaterialTheme.typography.bodyLarge)
        Text("Tipo de Plan: Estudiantil / Regular", style = MaterialTheme.typography.bodyMedium)
    }
}