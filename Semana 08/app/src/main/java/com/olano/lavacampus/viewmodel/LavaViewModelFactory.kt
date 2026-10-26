package com.olano.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olano.lavacampus.data.prefs.PreferenciasStore
import com.olano.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) ->
            ServiciosViewModel(repo) as T

        modelClass.isAssignableFrom(SesionViewModel::class.java) ->
            SesionViewModel(repo, prefs) as T

        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}