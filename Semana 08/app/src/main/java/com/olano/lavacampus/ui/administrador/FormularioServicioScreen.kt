package com.olano.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.components.IconoServicio
import com.olano.lavacampus.ui.components.LavaButton
import com.olano.lavacampus.ui.components.LavaTextField
import com.olano.lavacampus.viewmodel.FormularioServicioViewModel

@Composable
fun FormularioServicioScreen(
    vm: FormularioServicioViewModel,
    onAtras: () -> Unit
) {
    val servicioOriginal by vm.servicio.collectAsState()
    val tieneDetalles by vm.tieneDetalles.collectAsState()

    if (servicioOriginal == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando servicio...")
        }
    } else {
        val s = servicioOriginal!!

        var nombre by remember(s) { mutableStateOf(s.nombre) }
        var precioTexto by remember(s) { mutableStateOf(if (s.precio > 0) s.precio.toString() else "") }
        var unidad by remember(s) { mutableStateOf(s.unidad) }
        var icono by remember(s) { mutableStateOf(s.icono) }
        var horasTexto by remember(s) { mutableStateOf(s.horasEntrega.toString()) }
        var activo by remember(s) { mutableStateOf(s.activo) }
        var descripcion by remember(s) { mutableStateOf(s.descripcion) }

        var expUnidad by remember { mutableStateOf(false) }
        var expIcono by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                AppTopBar(
                    titulo = if (s.id == 0) "Nuevo Servicio" else "Editar Servicio",
                    onAtras = onAtras
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LavaTextField(
                    valor = nombre,
                    onValorChange = { nombre = it },
                    etiqueta = "Nombre del servicio"
                )

                LavaTextField(
                    valor = precioTexto,
                    onValorChange = { precioTexto = it },
                    etiqueta = "Precio (S/)",
                    teclado = KeyboardType.Number
                )

                // Dropdown Unidad
                Box {
                    OutlinedTextField(
                        value = unidad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unidad de medida") },
                        modifier = Modifier.fillMaxWidth().clickable { expUnidad = true },
                        shape = RoundedCornerShape(14.dp)
                    )
                    DropdownMenu(expanded = expUnidad, onDismissRequest = { expUnidad = false }) {
                        listOf("KG", "PRENDA", "UNIDAD").forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u) },
                                onClick = { unidad = u; expUnidad = false }
                            )
                        }
                    }
                }

                // Dropdown Icono
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expIcono = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconoServicio(icono = icono)
                        OutlinedTextField(
                            value = icono,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Icono") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                    DropdownMenu(expanded = expIcono, onDismissRequest = { expIcono = false }) {
                        listOf("wash", "iron", "shirt").forEach { ic ->
                            DropdownMenuItem(
                                text = { Text(ic) },
                                onClick = { icono = ic; expIcono = false }
                            )
                        }
                    }
                }

                LavaTextField(
                    valor = horasTexto,
                    onValorChange = { horasTexto = it },
                    etiqueta = "Horas estimadas de entrega",
                    teclado = KeyboardType.Number
                )

                LavaTextField(
                    valor = descripcion,
                    onValorChange = { descripcion = it },
                    etiqueta = "Descripción breve"
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Servicio activo", fontWeight = FontWeight.Bold)
                        Switch(checked = activo, onCheckedChange = { activo = it })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LavaButton(
                    texto = "Guardar servicio",
                    onClick = {
                        val p = precioTexto.toDoubleOrNull() ?: 0.0
                        val h = horasTexto.toIntOrNull() ?: 24
                        val nuevo = s.copy(
                            nombre = nombre.trim(),
                            precio = p,
                            unidad = unidad,
                            icono = icono,
                            horasEntrega = h,
                            activo = activo,
                            descripcion = descripcion.trim()
                        )
                        vm.guardar(nuevo, onAtras)
                    }
                )

                if (s.id > 0 && !tieneDetalles) {
                    OutlinedButton(
                        onClick = { vm.eliminar(s, onAtras) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar servicio")
                    }
                }
            }
        }
    }
}