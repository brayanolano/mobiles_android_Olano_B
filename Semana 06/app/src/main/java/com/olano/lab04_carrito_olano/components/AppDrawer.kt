package com.olano.lab04_carrito_olano.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    itemSeleccionado: String,
    onItemSelected: (String) -> Unit
) {
    ModalDrawerSheet {
        // Encabezado del menú lateral
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Círculo con tus iniciales (BO = Brayan Olano)
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB0BEC5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("BO", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    // Cambia estos valores por tu nombre y correo real
                    Text("Brayan Olano", style = MaterialTheme.typography.titleMedium)
                    Text("brayan.olano@tecsups.edu.pe", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }

        HorizontalDivider()

        // Opciones del menú
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = itemSeleccionado == "Inicio",
            onClick = { onItemSelected("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = itemSeleccionado == "Mis pedidos",
            onClick = { onItemSelected("Mis pedidos") },
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = itemSeleccionado == "Favoritos",
            onClick = { onItemSelected("Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = itemSeleccionado == "Perfil",
            onClick = { onItemSelected("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = itemSeleccionado == "Cerrar sesión",
            onClick = { onItemSelected("Cerrar sesión") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}