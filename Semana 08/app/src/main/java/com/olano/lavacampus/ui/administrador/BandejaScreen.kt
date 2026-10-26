package com.olano.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.components.PedidoCard
import com.olano.lavacampus.viewmodel.BandejaAdminViewModel

private val listaFiltros = listOf("TODOS", "RECIBIDO", "EN_PROCESO", "LISTO", "ENTREGADO", "CANCELADO")

@Composable
fun BandejaScreen(
    vm: BandejaAdminViewModel,
    onAtras: () -> Unit,
    onGestionarPedido: (Int) -> Unit
) {
    val filtroActual by vm.estadoFiltro.collectAsState()
    val pedidos by vm.pedidos.collectAsState()

    Scaffold(
        topBar = { AppTopBar(titulo = "Bandeja de Pedidos", onAtras = onAtras) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaFiltros) { f ->
                    val seleccionado = f == filtroActual
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.clickable { vm.cambiarFiltro(f) }
                    ) {
                        Text(
                            text = f,
                            color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { p ->
                    PedidoCard(
                        pedidoConDetalle = p,
                        onClick = { onGestionarPedido(p.pedido.id) }
                    )
                }
            }
        }
    }
}