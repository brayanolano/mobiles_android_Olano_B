package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ClientesViewModel(private val repo: LavaRepository) : ViewModel() {

    val clientes: StateFlow<List<ClienteResumen>> = repo.observarClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}