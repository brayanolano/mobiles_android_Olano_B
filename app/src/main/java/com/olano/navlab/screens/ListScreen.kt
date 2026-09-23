package com.olano.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val alumnos = (1..15).toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Directorio de Alumnos") },
                navigationIcon = { TextButton(onClick = onBack) { Text("< Atrás") } }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            items(alumnos) { id ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onItemClick(id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(text = "Alumno ID: $id", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}