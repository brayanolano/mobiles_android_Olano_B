package com.olano.lavacampus.data.repository

import androidx.room.withTransaction
import com.olano.lavacampus.data.local.LavaDatabase
import com.olano.lavacampus.data.local.entity.DetallePedido
import com.olano.lavacampus.data.local.entity.Pedido
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.data.local.entity.Usuario
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import com.olano.lavacampus.model.ClienteResumen
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.model.IngresoServicio
import com.olano.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {

    // Usuario
    override suspend fun login(correo: String, clave: String): Usuario? =
        db.usuarioDao().login(correo, clave)

    override fun observarClientes(): Flow<List<ClienteResumen>> =
        db.usuarioDao().observarClientes()

    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> =
        db.servicioDao().observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> =
        db.servicioDao().observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            db.servicioDao().insertar(s)
        } else {
            db.servicioDao().actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) =
        db.servicioDao().eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) =
        db.servicioDao().cambiarActivo(id, activo)

    override suspend fun servicioTieneDetalles(id: Int): Boolean =
        db.detalleDao().contarPorServicio(id) > 0

    // Pedidos
    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long = db.withTransaction {
        val hoy = LocalDate.now().toString()
        var total = 0.0
        val detallesAInsertar = mutableListOf<DetallePedido>()

        for (linea in lineas) {
            val s = db.servicioDao().obtener(linea.servicioId) ?: continue
            val subtotal = linea.cantidad * s.precio
            total += subtotal
            detallesAInsertar.add(
                DetallePedido(
                    pedidoId = 0,
                    servicioId = s.id,
                    cantidad = linea.cantidad,
                    precioUnitario = s.precio,
                    subtotal = subtotal
                )
            )
        }

        val pedidoId = db.pedidoDao().insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = hoy,
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = total,
                casillero = null,
                pagado = false,
                nota = nota
            )
        )

        db.detalleDao().insertarTodos(
            detallesAInsertar.map { it.copy(pedidoId = pedidoId.toInt()) }
        )

        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int =
        db.pedidoDao().contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> =
        db.pedidoDao().observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> =
        db.pedidoDao().observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> =
        db.pedidoDao().observarUno(id)

    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) =
        db.pedidoDao().actualizarEstado(id, estado.name, casillero)

    override suspend fun marcarPagado(id: Int, pagado: Boolean) =
        db.pedidoDao().actualizarPagado(id, pagado)

    override fun observarCasillerosOcupados(): Flow<List<String>> =
        db.pedidoDao().observarCasillerosOcupados()

    // Panel
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> =
        db.pedidoDao().contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        db.pedidoDao().contarPorEstado(estado.name)

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        db.pedidoDao().ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> =
        db.detalleDao().observarIngresosPorServicio(fechaInicio)
}