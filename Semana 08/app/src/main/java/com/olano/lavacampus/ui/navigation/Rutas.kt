package com.olano.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val DETALLE_PEDIDO = "detalle_pedido/{pedidoId}"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val PERFIL = "perfil"
    const val PANEL = "panel_admin"
    const val BANDEJA = "bandeja_admin"
    const val GESTION_PEDIDO = "gestion_pedido/{pedidoId}"
    const val SERVICIOS = "servicios_admin"
    const val FORMULARIO_SERVICIO = "formulario_servicio/{servicioId}"
    const val CLIENTES = "clientes_admin"
    const val AJUSTES = "ajustes_admin"

    fun detallePedido(id: Int) = "detalle_pedido/$id"
    fun gestionPedido(id: Int) = "gestion_pedido/$id"
    fun formularioServicio(id: Int) = "formulario_servicio/$id"
}