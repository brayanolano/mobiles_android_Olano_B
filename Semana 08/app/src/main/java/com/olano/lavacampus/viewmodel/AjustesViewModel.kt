package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(private val prefs: PreferenciasStore) : ViewModel() {

    val perfilActivo: StateFlow<String> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activo) }
    }

    fun cambiarNotificaciones(activo: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activo) }
    }

    fun cambiarRecibirPedidos(activo: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(activo) }
    }

    fun cambiarPerfil(rol: String, onExito: (() -> Unit)? = null) {
        viewModelScope.launch {
            val nuevoUsuarioId = if (rol == "ADMIN") 4 else 1
            prefs.guardarSesion(rol, nuevoUsuarioId)
            onExito?.invoke()
        }
    }

    fun cerrarSesion(onExito: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            onExito()
        }
    }
}