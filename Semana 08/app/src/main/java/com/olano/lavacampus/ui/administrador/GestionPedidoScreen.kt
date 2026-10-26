package com.olano.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.components.EstadoPedidoChip
import com.olano.lavacampus.ui.components.LavaButton
import com.olano.lavacampus.viewmodel.GestionPedidoViewModel

private val casillerosDisponibles = listOf("C-01", "C-02", "C-03", "C-04", "C-05", "C-06", "C-07", "C-08", "C-09", "C-10")

@Composable
fun GestionPedidoScreen(
    vm: GestionPedidoViewModel,
    onAtras: () -> Unit
) {
    val pedidoConDetalle by vm.pedidoConDetalle.collectAsState()
    val casillerosOcupados by vm.casillerosOcupados.collectAsState()

    if (pedidoConDetalle == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando información...")
        }
    } else {
        val p = pedidoConDetalle!!.pedido
        val usuario = pedidoConDetalle!!.usuario

        var estadoSeleccionado by remember(p.estado) {
            mutableStateOf(try { EstadoPedido.valueOf(p.estado) } catch (e: Exception) { EstadoPedido.RECIBIDO })
        }
        var casilleroSeleccionado by remember(p.casillero) { mutableStateOf(p.casillero ?: "C-01") }

        Scaffold(
            topBar = { AppTopBar(titulo = "Gestionar Pedido #%03d".format(p.id), onAtras = onAtras) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = "Estudiante: ${usuario.nombre}", fontWeight = FontWeight.Bold)
                        Text(text = "Código: ${usuario.codigo}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Correo: ${usuario.correo}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Actualizar estado del pedido",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        EstadoPedido.entries.forEach { e ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { estadoSeleccionado = e }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    EstadoPedidoChip(estado = e)
                                }
                                if (estadoSeleccionado == e) {
                                    Text("✓ Seleccionado", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                if (estadoSeleccionado == EstadoPedido.LISTO) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "Asignar Casillero",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Selecciona un casillero desocupado",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(casillerosDisponibles) { c ->
                                    val ocupado = casillerosOcupados.contains(c) && p.casillero != c
                                    val seleccionado = casilleroSeleccionado == c

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = when {
                                            ocupado -> MaterialTheme.colorScheme.errorContainer
                                            seleccionado -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        modifier = Modifier.clickable(enabled = !ocupado) {
                                            casilleroSeleccionado = c
                                        }
                                    ) {
                                        Text(
                                            text = if (ocupado) "$c (Ocupado)" else c,
                                            color = when {
                                                ocupado -> MaterialTheme.colorScheme.onErrorContainer
                                                seleccionado -> MaterialTheme.colorScheme.onPrimary
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Estado del Pago", fontWeight = FontWeight.Bold)
                            Text(
                                text = if (p.pagado) "El pedido está pagado" else "Pendiente de pago",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Switch(
                            checked = p.pagado,
                            onCheckedChange = { vm.cambiarPago(it) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LavaButton(
                    texto = "Guardar cambios",
                    onClick = {
                        val casilleroFinal = if (estadoSeleccionado == EstadoPedido.LISTO) casilleroSeleccionado else null
                        vm.actualizarEstado(estadoSeleccionado, casilleroFinal) {
                            onAtras()
                        }
                    }
                )
            }
        }
    }
}