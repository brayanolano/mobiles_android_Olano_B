package com.olano.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelAdminViewModel(private val repo: LavaRepository) : ViewModel() {
    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recibidos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val listos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val datosEstados: StateFlow<List<DatoGrafico>> = combine(recibidos, enProceso, listos) { r, p, l ->
        listOf(
            DatoGrafico("Recibidos", r.toFloat(), Color(0xFFF59E0B)),
            DatoGrafico("En proceso", p.toFloat(), Color(0xFF3B82F6)),
            DatoGrafico("Listos", l.toFloat(), Color(0xFF10B981))
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val datosIngresosServicios: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(inicioMes)
        .map { ingresos ->
            ingresos.mapIndexed { idx, ing ->
                val col = when (idx % 4) {
                    0 -> Color(0xFF0891B2)
                    1 -> Color(0xFF6D28D9)
                    2 -> Color(0xFF059669)
                    else -> Color(0xFFD97706)
                }
                DatoGrafico(ing.nombre, ing.total.toFloat(), col)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}