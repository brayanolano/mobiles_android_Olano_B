package com.olano.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    usuarioNombre: String = "Brayan Olano",
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(usuarioNombre, style = MaterialTheme.typography.titleLarge)
            Text("Paciente", style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = false,
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                onClick = { onNavigate("inicio"); onCloseDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Mis Citas") },
                selected = false,
                icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                onClick = { onNavigate("mis_citas"); onCloseDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Historial médico") },
                selected = false,
                icon = { Icon(Icons.Default.History, contentDescription = null) },
                onClick = { onNavigate("historial"); onCloseDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Perfil") },
                selected = false,
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                onClick = { onNavigate("perfil"); onCloseDrawer() }
            )
        }
    }
}