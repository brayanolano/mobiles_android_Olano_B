package com.olano.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedicoScreen(
    doctorId: Int,
    onBack: () -> Unit,
    onAgendarClick: (String) -> Unit
) {
    val doctorNombre = if (doctorId == 1) "Dra. Ana Torres" else if (doctorId == 2) "Dr. Luis Vega" else "Dra. Rosa Díaz"
    val especialidad = if (doctorId == 1) "Cardióloga" else if (doctorId == 2) "Pediatra" else "Dermatóloga"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(doctorNombre, style = MaterialTheme.typography.headlineSmall)
            Text("$especialidad • 12 años exp.", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Especialista en medicina integral con amplia experiencia laboral en el sector académico y privado.")
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onAgendarClick(doctorNombre) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar cita")
            }
        }
    }
}