package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    val pedidoId: Int
) : ViewModel() {

    val pedidoConDetalle: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val casillerosOcupados: StateFlow<List<String>> = repo.observarCasillerosOcupados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun actualizarEstado(nuevoEstado: EstadoPedido, casillero: String?, onExito: () -> Unit) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, nuevoEstado, casillero)
            onExito()
        }
    }

    fun cambiarPago(pagado: Boolean) {
        viewModelScope.launch {
            repo.marcarPagado(pedidoId, pagado)
        }
    }
}