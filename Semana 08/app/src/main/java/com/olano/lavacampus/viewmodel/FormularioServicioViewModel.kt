package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioServicioViewModel(
    private val repo: LavaRepository,
    val servicioId: Int
) : ViewModel() {

    private val _servicio = MutableStateFlow<Servicio?>(null)
    val servicio: StateFlow<Servicio?> = _servicio.asStateFlow()

    private val _tieneDetalles = MutableStateFlow(false)
    val tieneDetalles: StateFlow<Boolean> = _tieneDetalles.asStateFlow()

    init {
        if (servicioId > 0) {
            viewModelScope.launch {
                repo.observarServicios().collect { lista ->
                    val s = lista.firstOrNull { it.id == servicioId }
                    _servicio.value = s
                }
            }
            viewModelScope.launch {
                _tieneDetalles.value = repo.servicioTieneDetalles(servicioId)
            }
        } else {
            _servicio.value = Servicio(
                id = 0,
                nombre = "",
                precio = 0.0,
                unidad = "KG",
                icono = "wash",
                horasEntrega = 24,
                activo = true,
                descripcion = ""
            )
        }
    }

    fun guardar(servicio: Servicio, onExito: () -> Unit) {
        viewModelScope.launch {
            repo.guardarServicio(servicio)
            onExito()
        }
    }

    fun eliminar(servicio: Servicio, onExito: () -> Unit) {
        viewModelScope.launch {
            repo.eliminarServicio(servicio)
            onExito()
        }
    }
}