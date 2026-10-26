package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BandejaAdminViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _estadoFiltro = MutableStateFlow("TODOS")
    val estadoFiltro: StateFlow<String> = _estadoFiltro.asStateFlow()

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(),
        _estadoFiltro
    ) { lista, filtro ->
        if (filtro == "TODOS") lista
        else lista.filter { it.pedido.estado == filtro }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun cambiarFiltro(nuevo: String) {
        _estadoFiltro.value = nuevo
    }

    fun seleccionarFiltro(nuevo: EstadoPedido?) {
        _estadoFiltro.value = nuevo?.name ?: "TODOS"
    }

    fun cambiarEstado(pedidoId: Int, nuevoEstado: EstadoPedido, casillero: String? = null) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, nuevoEstado, casillero)
        }
    }
}