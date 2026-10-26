package com.olano.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.viewmodel.AjustesViewModel

@Composable
fun AjustesScreen(
    vm: AjustesViewModel,
    onAtras: () -> Unit,
    onCerrarSesion: () -> Unit,
    onCambiarPerfilExito: ((String) -> Unit)? = null
) {
    val perfilActivo by vm.perfilActivo.collectAsState()
    val modoOscuro by vm.modoOscuro.collectAsState()
    val notificaciones by vm.notificaciones.collectAsState()
    val recibirPedidos by vm.recibirPedidos.collectAsState()

    Scaffold(
        topBar = { AppTopBar(titulo = "Ajustes y Configuración", onAtras = onAtras) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preferencias de la aplicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Modo oscuro", fontWeight = FontWeight.SemiBold)
                        Switch(checked = modoOscuro, onCheckedChange = { vm.cambiarModoOscuro(it) })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Notificaciones activas", fontWeight = FontWeight.SemiBold)
                        Switch(checked = notificaciones, onCheckedChange = { vm.cambiarNotificaciones(it) })
                    }

                    if (perfilActivo == "ADMIN") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Recibir nuevos pedidos", fontWeight = FontWeight.SemiBold)
                            Switch(checked = recibirPedidos, onCheckedChange = { vm.cambiarRecibirPedidos(it) })
                        }
                    }
                }
            }

            Text(
                text = "Cambiar perfil activo (Modo prueba)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            vm.cambiarPerfil("CLIENTE") {
                                onCambiarPerfilExito?.invoke("CLIENTE")
                            }
                        },
                        enabled = perfilActivo != "CLIENTE",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cambiar a Estudiante")
                    }

                    OutlinedButton(
                        onClick = {
                            vm.cambiarPerfil("ADMIN") {
                                onCambiarPerfilExito?.invoke("ADMIN")
                            }
                        },
                        enabled = perfilActivo != "ADMIN",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cambiar a Administrador")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { vm.cerrarSesion(onCerrarSesion) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}