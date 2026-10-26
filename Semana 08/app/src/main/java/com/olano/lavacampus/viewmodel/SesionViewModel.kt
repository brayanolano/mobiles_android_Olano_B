package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.local.entity.Usuario
import com.olano.lavacampus.data.prefs.PreferenciasStore
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilGuardado: StateFlow<String> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa el correo y la contraseña"
            return
        }

        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave.trim())
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (usuario.rol != rolSeleccionado.name) {
                _error.value = "El usuario no corresponde al perfil seleccionado"
            } else {
                _error.value = null
                _usuarioActual.value = usuario
                prefs.guardarSesion(usuario.rol, usuario.id)
                onExito(rolSeleccionado)
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
        }
    }
}