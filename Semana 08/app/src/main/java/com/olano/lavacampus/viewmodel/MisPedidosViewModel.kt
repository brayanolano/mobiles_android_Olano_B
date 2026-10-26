package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MisPedidosViewModel(
    private val repo: LavaRepository,
    val usuarioId: Int
) : ViewModel() {

    val pedidos: StateFlow<List<PedidoConDetalle>> = repo.observarPedidosDeUsuario(usuarioId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}