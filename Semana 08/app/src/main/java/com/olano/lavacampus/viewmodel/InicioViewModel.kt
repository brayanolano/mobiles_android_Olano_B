package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InicioViewModel(
    private val repo: LavaRepository,
    val usuarioId: Int
) : ViewModel() {

    // Pedido activo: El más reciente en estado RECIBIDO, EN_PROCESO o LISTO
    val pedidoActivo: StateFlow<PedidoConDetalle?> = repo.observarPedidosDeUsuario(usuarioId)
        .map { lista ->
            lista.firstOrNull { p ->
                p.pedido.estado == EstadoPedido.RECIBIDO.name ||
                        p.pedido.estado == EstadoPedido.EN_PROCESO.name ||
                        p.pedido.estado == EstadoPedido.LISTO.name
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Últimos 2 pedidos
    val recientes: StateFlow<List<PedidoConDetalle>> = repo.observarPedidosDeUsuario(usuarioId)
        .map { lista -> lista.take(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}