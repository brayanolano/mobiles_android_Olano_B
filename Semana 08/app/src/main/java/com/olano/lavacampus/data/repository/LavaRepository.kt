package com.olano.lavacampus.data.repository

import com.olano.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de datos de la app.
 * En el proyecto base solo está activa la sección de Servicios (ejemplo resuelto).
 */
interface LavaRepository {
    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}