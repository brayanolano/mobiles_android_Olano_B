package com.olano.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    doctorNombre: String,
    onBack: () -> Unit,
    onConfirmar: (String, String) -> Unit
) {
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00", "10:30", "3:00")

    var fechaSel by remember { mutableStateOf("Vie 27") }
    var horaSel by remember { mutableStateOf("10:30") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
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
                .padding(24.dp)
        ) {
            Text("Selecciona fecha", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fechas.forEach { f ->
                    FilterChip(
                        selected = f == fechaSel,
                        onClick = { fechaSel = f },
                        label = { Text(f) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Selecciona hora", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                horas.forEach { h ->
                    FilterChip(
                        selected = h == horaSel,
                        onClick = { horaSel = h },
                        label = { Text(h) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onConfirmar(fechaSel, horaSel) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}

@Composable
fun ConfirmacionScreen(
    doctorNombre: String,
    fecha: String,
    hora: String,
    onVolverInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineMedium)
        Text(doctorNombre, style = MaterialTheme.typography.titleLarge)
        Text("$fecha, $hora", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onVolverInicio) {
            Text("Ver mis citas")
        }
    }
}