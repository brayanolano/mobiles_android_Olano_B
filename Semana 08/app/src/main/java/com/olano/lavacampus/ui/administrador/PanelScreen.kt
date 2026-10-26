package com.olano.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.ui.components.graficos.GraficoBarras
import com.olano.lavacampus.ui.components.graficos.GraficoDona
import com.olano.lavacampus.viewmodel.PanelAdminViewModel

@Composable
fun PanelScreen(
    vm: PanelAdminViewModel,
    onIrBandeja: () -> Unit,
    onIrServicios: () -> Unit,
    onIrClientes: () -> Unit,
    onIrAjustes: () -> Unit
) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val ingresosMes by vm.ingresosMes.collectAsState()
    val datosEstados by vm.datosEstados.collectAsState()
    val datosIngresos by vm.datosIngresosServicios.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Panel de Control",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Resumen de operaciones y métricas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Tarjetas KPI
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiCard(
                    titulo = "Pedidos hoy",
                    valor = pedidosHoy.toString(),
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    titulo = "Ingresos del mes",
                    valor = "S/ %.2f".format(ingresosMes),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Accesos de Administración
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Gestión del Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BotonMenuAdmin(
                        texto = "Bandeja",
                        icono = Icons.Filled.Inbox,
                        onClick = onIrBandeja,
                        modifier = Modifier.weight(1f)
                    )
                    BotonMenuAdmin(
                        texto = "Servicios",
                        icono = Icons.Filled.DryCleaning,
                        onClick = onIrServicios,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BotonMenuAdmin(
                        texto = "Clientes",
                        icono = Icons.Filled.Group,
                        onClick = onIrClientes,
                        modifier = Modifier.weight(1f)
                    )
                    BotonMenuAdmin(
                        texto = "Ajustes",
                        icono = Icons.Filled.Settings,
                        onClick = onIrAjustes,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Gráfico de Dona: Estado de Pedidos
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        text = "Distribución de Pedidos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GraficoDona(datos = datosEstados, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        // Gráfico de Barras: Ingresos por Servicio
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        text = "Ingresos por Servicio (S/)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GraficoBarras(datos = datosIngresos, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun KpiCard(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun BotonMenuAdmin(
    texto: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icono, contentDescription = texto, tint = MaterialTheme.colorScheme.primary)
            Text(text = texto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        }
    }
}