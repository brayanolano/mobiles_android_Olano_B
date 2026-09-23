package com.olano.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olano.navlab.model.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onOpenDrawer: () -> Unit,
    onDoctorSelect: (Int) -> Unit
) {
    val especialidades = listOf("Cardiología", "Pediatría", "Dermatología", "Neurología")
    var especialidadSeleccionada by remember { mutableStateOf("Cardiología") }

    val medicos = listOf(
        Doctor(1, "Dra. Ana Torres", "Cardiología", "12 años exp.", 4.9, 128, "Especialista en arritmias e hipertensión."),
        Doctor(2, "Dr. Luis Vega", "Pediatría", "8 años exp.", 4.7, 95, "Atención integral infantil y desarrollo."),
        Doctor(3, "Dra. Rosa Díaz", "Dermatología", "10 años exp.", 4.8, 110, "Especialista en dermatología clínica y estética.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clínica Salud+\nHola, Brayan") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(especialidades) { esp ->
                    FilterChip(
                        selected = esp == especialidadSeleccionada,
                        onClick = { especialidadSeleccionada = esp },
                        label = { Text(esp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicos) { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDoctorSelect(doc.id) },
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(doc.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(doc.especialidad, style = MaterialTheme.typography.bodyMedium)
                            }
                            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(" ${doc.calificacion}")
                        }
                    }
                }
            }
        }
    }
}