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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class BandejaAdminViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _estadoFiltro = MutableStateFlow("TODOS")
    val estadoFiltro: StateFlow<String> = _estadoFiltro.asStateFlow()

    private val hoy = LocalDate.now().toString()

    // Exponer el conjunto de IDs de pedidos atrasados
    val idsPedidosAtrasados: StateFlow<Set<Int>> = repo.observarIdsPedidosAtrasados(hoy)
        .map { it.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    // Exponer la lista de pedidos atrasados
    val pedidosAtrasados: StateFlow<List<PedidoConDetalle>> = repo.observarPedidosAtrasados(hoy)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Exponer la cantidad total de pedidos atrasados
    val cantidadAtrasados: StateFlow<Int> = repo.contarPedidosAtrasados(hoy)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(),
        _estadoFiltro
    ) { lista, filtro ->
        when (filtro) {
            "TODOS" -> lista
            "ATRASADOS" -> lista.filter { p ->
                p.pedido.fechaEntrega < hoy && p.pedido.estado != "ENTREGADO" && p.pedido.estado != "CANCELADO"
            }
            else -> lista.filter { it.pedido.estado == filtro }
        }
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