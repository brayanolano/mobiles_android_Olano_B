package com.olano.lavacampus.model

data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val correo: String,
    val codigo: String,
    val totalPedidos: Int,
    val pedidosActivos: Int,
    val totalGastado: Double
)