package com.olano.lavacampus.ui.estudiante

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.ui.components.LavaButton
import com.olano.lavacampus.ui.components.PedidoCard
import com.olano.lavacampus.ui.components.StepperEstado
import com.olano.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onVerPedidos: () -> Unit,
    onVerTarifas: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()

    // Nombre y código del usuario obtenidos desde el último pedido o por defecto
    val usuarioNombre = recientes.firstOrNull()?.usuario?.nombre ?: "Estudiante"
    val usuarioCodigo = recientes.firstOrNull()?.usuario?.codigo ?: "U202310234"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera de bienvenida
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hola, ${usuarioNombre.split(" ").firstOrNull() ?: usuarioNombre}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$usuarioCodigo · Tecsup",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Tarjeta turquesa del pedido activo
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    if (activo != null) {
                        val p = activo!!.pedido
                        val estadoEnum = EstadoPedido.valueOf(p.estado)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tu pedido activo",
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "#%03d".format(p.id),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.colorScheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (estadoEnum == EstadoPedido.LISTO && !p.casillero.isNull_or_empty()) {
                                "Recógelo en el casillero ${p.casillero}"
                            } else {
                                estadoEnum.etiqueta
                            },
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = "Entrega estimada: ${p.fechaEntrega}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        StepperEstado(estado = estadoEnum, horizontal = true)
                    } else {
                        Text(
                            text = "Tu pedido activo",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No tienes pedidos activos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Arma tu pedido de lavado o planchado cuando quieras",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Botón 'Nuevo pedido'
        item {
            LavaButton(
                texto = "Nuevo pedido",
                onClick = onNuevoPedido
            )
        }

        // Accesos rápidos (Mis pedidos / Tarifas)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccesoRapidoCard(
                    titulo = "Mis pedidos",
                    subtitulo = "Ver estado",
                    icono = Icons.Filled.ListAlt,
                    onClick = onVerPedidos,
                    modifier = Modifier.weight(1f)
                )
                AccesoRapidoCard(
                    titulo = "Tarifas",
                    subtitulo = "Precios por kg",
                    icono = Icons.Filled.Sell,
                    onClick = onVerTarifas,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Sección 'Últimos pedidos'
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos pedidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onVerPedidos) {
                    Text("Ver todos")
                }
            }
        }

        items(recientes, key = { it.pedido.id }) { p ->
            PedidoCard(
                pedidoConDetalle = p,
                onClick = { onVerDetalle(p.pedido.id) }
            )
        }
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.isEmpty()

@Composable
private fun AccesoRapidoCard(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}