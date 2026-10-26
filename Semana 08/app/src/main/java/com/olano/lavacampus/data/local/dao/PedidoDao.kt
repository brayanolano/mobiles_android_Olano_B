package com.olano.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.olano.lavacampus.data.local.entity.Pedido
import com.olano.lavacampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pedido: Pedido): Long

    @Query("SELECT COUNT(*) FROM pedidos WHERE usuarioId = :usuarioId AND estado IN ('RECIBIDO', 'EN_PROCESO', 'LISTO')")
    suspend fun contarActivos(usuarioId: Int): Int

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY fechaCreacion DESC")
    fun observarTodos(): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY fechaCreacion DESC")
    fun observarTodosConDetalle(): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun observarPorUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    fun observarUno(pedidoId: Int): Flow<PedidoConDetalle?>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    fun observarPorId(pedidoId: Int): Flow<PedidoConDetalle?>

    @Query("UPDATE pedidos SET estado = :nuevoEstado, casillero = :casillero WHERE id = :pedidoId")
    suspend fun actualizarEstado(pedidoId: Int, nuevoEstado: String, casillero: String?)

    @Query("UPDATE pedidos SET pagado = :pagado WHERE id = :pedidoId")
    suspend fun actualizarPagado(pedidoId: Int, pagado: Boolean)

    @Query("SELECT DISTINCT casillero FROM pedidos WHERE estado = 'LISTO' AND casillero IS NOT NULL AND casillero != ''")
    fun observarCasillerosOcupados(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM pedidos WHERE fechaRegistro = :fecha")
    fun contarPorFecha(fecha: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE pagado = 1 AND fechaRegistro >= :fechaInicio")
    fun ingresosDesde(fechaInicio: String): Flow<Double>

    // Pedidos Atrasados (fechaEntrega < fechaHoy Y estado NOT IN ('ENTREGADO', 'CANCELADO'))
    @Transaction
    @Query("SELECT * FROM pedidos WHERE fechaEntrega < :fechaHoy AND estado NOT IN ('ENTREGADO', 'CANCELADO') ORDER BY fechaEntrega ASC")
    fun observarPedidosAtrasados(fechaHoy: String): Flow<List<PedidoConDetalle>>

    @Query("SELECT id FROM pedidos WHERE fechaEntrega < :fechaHoy AND estado NOT IN ('ENTREGADO', 'CANCELADO')")
    fun observarIdsPedidosAtrasados(fechaHoy: String): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM pedidos WHERE fechaEntrega < :fechaHoy AND estado NOT IN ('ENTREGADO', 'CANCELADO')")
    fun contarPedidosAtrasados(fechaHoy: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM pedidos WHERE fechaEntrega < :fechaHoy AND estado NOT IN ('ENTREGADO', 'CANCELADO')")
    suspend fun contarAtrasadosSync(fechaHoy: String): Int
}