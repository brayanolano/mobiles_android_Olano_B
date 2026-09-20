package com.olano.carrito_olano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.olano.carrito_olano.ui.theme.CarritoolanoTheme
import com.olano.carrito_olano.ui.theme.CarritoolanoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarritoolanoTheme() {
                PantallaCarrito()
            }
        }
    }
}

/**
 * Pantalla principal del carrito de compras.
 * Contiene el formulario, la lista de productos (LazyColumn) y el panel de totales.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito() {
    // --- Estados del formulario ---
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    // Lista observable: gracias a mutableStateListOf(), Compose recompone
    // automáticamente la UI cuando se agrega o elimina un producto.
    val productos = remember { mutableStateListOf<Producto>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito TECSUP") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6C5CA5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        // Un solo Column exterior: formulario + lista/estado-vacío + panel
        // van dentro de él, en orden, de arriba hacia abajo.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // --- Formulario (con su propio padding interno) ---
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = precio,
                        onValueChange = { precio = it },
                        label = { Text("Precio (S/)") },
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón AGREGAR: valida, agrega el producto a la lista y limpia el formulario
                Button(
                    onClick = {
                        val precioNum = precio.toDoubleOrNull() ?: 0.0
                        val cantidadNum = cantidad.toIntOrNull() ?: 0
                        if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                            productos.add(Producto(nombre, precioNum, cantidadNum))
                            nombre = ""
                            precio = ""
                            cantidad = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CA5))
                ) {
                    Text("AGREGAR")
                }
            }

            // --- Lista de productos / estado vacío ---
            // weight(1f) hace que esta sección ocupe TODO el espacio sobrante,
            // empujando el panel de totales hasta el fondo de la pantalla.
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Tu carrito está vacío",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text("Agrega tu primer producto", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onEliminar = { productos.remove(producto) }
                        )
                    }
                }
            }

            // --- Panel de totales: último elemento del Column, siempre visible abajo ---
            PanelTotales(productos)
        }
    }
}

/**
 * Tarjeta de un producto: nombre en negrita arriba, debajo "S/precio x cantidad"
 * en gris, y a la derecha el importe total en morado + botón eliminar.
 * No sabe CÓMO eliminar: solo avisa con onEliminar (patrón "elevar eventos").
 */
@Composable
fun TarjetaProducto(producto: Producto, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna izquierda: nombre + detalle (precio x cantidad)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/${"%.2f".format(producto.precio)}  x ${producto.cantidad}",
                    color = Color.Gray
                )
            }

            // Importe = precio x cantidad, con 2 decimales
            Text(
                text = "S/ ${"%.2f".format(producto.precio * producto.cantidad)}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C5CA5)
            )

            // Botón eliminar: solo notifica el evento hacia arriba
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Panel inferior con el resumen de la compra.
 * Se recalcula automáticamente porque lee del estado observable `productos`:
 * cada vez que se agrega o elimina un elemento, Compose vuelve a ejecutar
 * estos cálculos y repinta este panel.
 */
@Composable
fun PanelTotales(productos: List<Producto>) {
    val subtotal = productos.sumOf { it.precio * it.cantidad }
    val igv = subtotal * 0.18
    val total = subtotal + igv

    Surface(
        color = Color(0xFFEDE9F5), // fondo lila suave
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Productos: ${productos.size}", color = Color.Gray)

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal")
                Text("S/${"%.2f".format(subtotal)}")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("IGV (18%)")
                Text("S/${"%.2f".format(igv)}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "TOTAL",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "S/ ${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C5CA5)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaCarritoPreview() {
    CarritoolanoTheme() {
        PantallaCarrito()
    }
}