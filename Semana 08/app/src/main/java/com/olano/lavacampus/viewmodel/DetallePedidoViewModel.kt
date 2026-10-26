package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val repo: LavaRepository,
    val pedidoId: Int
) : ViewModel() {

    val pedidoConDetalle: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun simularPago() {
        viewModelScope.launch {
            repo.marcarPagado(pedidoId, true)
        }
    }
}