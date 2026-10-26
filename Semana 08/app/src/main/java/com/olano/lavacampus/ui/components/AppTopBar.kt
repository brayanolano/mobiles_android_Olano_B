package com.olano.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onAtras: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = titulo) },
        navigationIcon = {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        }
    )
}

data class ItemNav(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

sealed class DestinoEstudiante(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object Inicio : DestinoEstudiante("inicio", "Inicio", Icons.Filled.Home)
    object Pedidos : DestinoEstudiante("mis_pedidos", "Mis pedidos", Icons.Filled.ListAlt)
    object Tarifas : DestinoEstudiante("tarifas", "Tarifas", Icons.Filled.Sell)
    object Perfil : DestinoEstudiante("perfil", "Perfil", Icons.Filled.Person)
}

sealed class DestinoAdmin(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object Panel : DestinoAdmin("panel_admin", "Panel", Icons.Filled.Dashboard)
    object Bandeja : DestinoAdmin("bandeja_admin", "Bandeja", Icons.Filled.Inbox)
    object Servicios : DestinoAdmin("servicios_admin", "Servicios", Icons.Filled.DryCleaning)
    object Clientes : DestinoAdmin("clientes_admin", "Clientes", Icons.Filled.Group)
    object Ajustes : DestinoAdmin("ajustes_admin", "Ajustes", Icons.Filled.Settings)
}

val seccionesEstudiante = listOf(
    DestinoEstudiante.Inicio,
    DestinoEstudiante.Pedidos,
    DestinoEstudiante.Tarifas,
    DestinoEstudiante.Perfil
)

val seccionesAdmin = listOf(
    DestinoAdmin.Panel,
    DestinoAdmin.Bandeja,
    DestinoAdmin.Servicios,
    DestinoAdmin.Clientes,
    DestinoAdmin.Ajustes
)

@Composable
fun AppBottomBar(
    rutaActual: String?,
    esAdmin: Boolean = false,
    onNavegar: (String) -> Unit
) {
    val items = if (esAdmin || rutaActual in listOf("panel_admin", "bandeja_admin", "servicios_admin", "clientes_admin", "ajustes_admin")) {
        seccionesAdmin.map { ItemNav(it.ruta, it.titulo, it.icono) }
    } else {
        seccionesEstudiante.map { ItemNav(it.ruta, it.titulo, it.icono) }
    }

    NavigationBar {
        items.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { onNavegar(item.ruta) },
                icon = {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.titulo
                    )
                },
                label = { Text(text = item.titulo) }
            )
        }
    }
}