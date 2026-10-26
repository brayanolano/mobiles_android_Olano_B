package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    val usuarioId: Int
) : ViewModel() {

    val serviciosActivos: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun actualizarCantidad(servicioId: Int, cantidad: Double) {
        val mapa = _cantidades.value.toMutableMap()
        if (cantidad <= 0.0) {
            mapa.remove(servicioId)
        } else {
            mapa[servicioId] = cantidad
        }
        _cantidades.value = mapa
        _error.value = null
    }

    val total: StateFlow<Double> = combine(serviciosActivos, cantidades) { servicios, cants ->
        servicios.sumOf { s -> (cants[s.id] ?: 0.0) * s.precio }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val fechaEntregaEstimada: StateFlow<String> = combine(serviciosActivos, cantidades) { servicios, cants ->
        val seleccionados = servicios.filter { (cants[it.id] ?: 0.0) > 0.0 }
        val maxHoras = seleccionados.maxOfOrNull { it.horasEntrega } ?: 24
        val dias = (maxHoras / 24).coerceAtLeast(1)

        var fecha = LocalDate.now().plusDays(dias.toLong())
        if (fecha.dayOfWeek == DayOfWeek.SUNDAY) {
            fecha = fecha.plusDays(1)
        }
        fecha.toString()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocalDate.now().plusDays(1).toString())

    fun confirmarPedido(nota: String, onExito: (Int) -> Unit) {
        val cants = _cantidades.value
        val lineas = cants.filter { it.value > 0.0 }.map { (servicioId, cantidad) ->
            LineaPedido(servicioId, cantidad)
        }

        if (lineas.isEmpty()) {
            _error.value = "Selecciona al menos un servicio"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            try {
                val activos = repo.contarActivos(usuarioId)
                if (activos >= 3) {
                    _error.value = "Tienes 3 pedidos activos. Recoge tus pedidos listos antes de solicitar uno nuevo."
                    _cargando.value = false
                    return@launch
                }

                val fechaEntrega = fechaEntregaEstimada.value
                val pedidoId = repo.crearPedido(usuarioId, lineas, fechaEntrega, nota.trim())
                _cargando.value = false
                onExito(pedidoId.toInt())
            } catch (e: Exception) {
                _cargando.value = false
                _error.value = "Error al crear el pedido: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}