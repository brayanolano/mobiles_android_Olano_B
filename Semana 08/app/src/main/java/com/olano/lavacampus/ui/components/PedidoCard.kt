package com.olano.lavacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.model.Unidad

@Composable
fun PedidoCard(
    pedidoConDetalle: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val p = pedidoConDetalle.pedido
    val detalles = pedidoConDetalle.detalles
    val estadoEnum = try {
        EstadoPedido.valueOf(p.estado)
    } catch (e: Exception) {
        EstadoPedido.RECIBIDO
    }

    val primerServicio = detalles.firstOrNull()?.servicio
    val icono = primerServicio?.icono ?: "wash"

    // Resumen breve de cantidades (acceso corregido con d.detalle.cantidad)
    val resumenCantidades = detalles.joinToString(" · ") { d ->
        val un = Unidad.desde(d.servicio.unidad)
        val cant = d.detalle.cantidad
        val cantTexto = if (cant % 1.0 == 0.0) cant.toInt().toString() else cant.toString()
        "$cantTexto ${un.etiqueta.lowercase()}"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconoServicio(icono = icono)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pedido #%03d".format(p.id),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "S/ %.2f".format(p.total),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (resumenCantidades.isNotEmpty()) "$resumenCantidades · ${p.fechaRegistro}" else p.fechaRegistro,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoPedidoChip(estado = estadoEnum)

                    if (estadoEnum == EstadoPedido.LISTO && !p.casillero.isNullOrEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Casillero ${p.casillero}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}