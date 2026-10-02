package com.olano.lab04_carrito_olano.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DrawerItem(
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun AppDrawer(
    itemSeleccionado: String,
    onItemSelected: (String) -> Unit
) {
    val items = listOf(
        DrawerItem("Inicio", Icons.Default.Home),
        DrawerItem("Mis pedidos", Icons.Default.List),
        DrawerItem("Favoritos", Icons.Default.Favorite),
        DrawerItem("Perfil", Icons.Default.Person),
        DrawerItem("Cerrar sesion", Icons.Default.ExitToApp)
    )

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MR",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Maria Rojas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "maria@tecsup.edu.pe",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.height(8.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                selected = item.titulo == itemSeleccionado,
                onClick = { onItemSelected(item.titulo) },
                icon = { Icon(item.icono, contentDescription = null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}