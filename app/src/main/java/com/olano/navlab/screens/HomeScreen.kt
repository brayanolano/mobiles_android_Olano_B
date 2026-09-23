package com.olano.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToList: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Portal Académico", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateToList, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Ver Directorio de Alumnos")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onNavigateToProfile, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Ir a Mi Perfil")
        }
    }
}