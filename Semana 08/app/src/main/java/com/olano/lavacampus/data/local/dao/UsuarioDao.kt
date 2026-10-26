package com.olano.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olano.lavacampus.data.local.entity.Usuario
import com.olano.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(usuarios: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Query("""
        SELECT 
            u.id AS id, 
            u.nombre AS nombre, 
            u.correo AS correo, 
            u.codigo AS codigo,
            COUNT(p.id) AS totalPedidos,
            SUM(CASE WHEN p.estado IN ('RECIBIDO', 'EN_PROCESO', 'LISTO') THEN 1 ELSE 0 END) AS pedidosActivos,
            COALESCE(SUM(p.total), 0.0) AS totalGastado
        FROM usuarios u
        LEFT JOIN pedidos p ON u.id = p.usuarioId
        WHERE u.rol = 'CLIENTE'
        GROUP BY u.id
    """)
    fun observarClientes(): Flow<List<ClienteResumen>>
}