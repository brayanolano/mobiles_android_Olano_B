package com.olano.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.model.Unidad
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.components.IconoServicio
import com.olano.lavacampus.ui.components.LavaButton
import com.olano.lavacampus.ui.components.LavaTextField
import com.olano.lavacampus.ui.components.SelectorCantidad
import com.olano.lavacampus.viewmodel.NuevoPedidoViewModel

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    onAtras: () -> Unit,
    onPedidoCreado: (Int) -> Unit
) {
    val servicios by vm.serviciosActivos.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val total by vm.total.collectAsState()
    val fechaEntrega by vm.fechaEntregaEstimada.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()

    var nota by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(titulo = "Nuevo pedido", onAtras = onAtras) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Selecciona los servicios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(servicios, key = { it.id }) { s ->
                    val cantidad = cantidades[s.id] ?: 0.0
                    ServicioSeleccionableItem(
                        servicio = s,
                        cantidad = cantidad,
                        onCantidadChange = { nuevaCant ->
                            vm.actualizarCantidad(s.id, nuevaCant)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Indicaciones o nota especial (opcional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LavaTextField(
                        valor = nota,
                        onValorChange = { nota = it },
                        etiqueta = "Ej. Separar ropa blanca, sin suavizante..."
                    )
                }

                if (error != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Entrega estimada",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = fechaEntrega,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Total a pagar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "S/ %.2f".format(total),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (cargando) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        LavaButton(
                            texto = "Confirmar pedido",
                            enabled = total > 0.0,
                            onClick = {
                                vm.confirmarPedido(nota) { pedidoId ->
                                    onPedidoCreado(pedidoId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicioSeleccionableItem(
    servicio: Servicio,
    cantidad: Double,
    onCantidadChange: (Double) -> Unit
) {
    val unidad = Unidad.desde(servicio.unidad)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconoServicio(icono = servicio.icono)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = servicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/ %.2f / %s · %dh".format(servicio.precio, unidad.etiqueta.lowercase(), servicio.horasEntrega),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            SelectorCantidad(
                valor = cantidad,
                paso = unidad.paso,
                minimo = unidad.minimo,
                maximo = 50.0,
                onValorChange = onCantidadChange
            )
        }
    }
}