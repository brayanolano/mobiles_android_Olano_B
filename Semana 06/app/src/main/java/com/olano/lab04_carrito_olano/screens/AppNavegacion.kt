package com.olano.lab04_carrito_olano.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.olano.lab04_carrito_olano.components.AppDrawer
import com.olano.lab04_carrito_olano.components.ItemCarrito
import com.olano.lab04_carrito_olano.components.TarjetaProducto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var itemSeleccionado by remember { mutableStateOf("Inicio") }

    var nombreProducto by remember { mutableStateOf("") }
    var precioProducto by remember { mutableStateOf("") }
    var cantidadProducto by remember { mutableStateOf("") }

    val listaProductos = remember {
        mutableStateListOf(
            ItemCarrito(1, "Laptop Gamer", 3000.00, 2)
        )
    }

    val subtotalGeneral = listaProductos.sumOf { it.precio * it.cantidad }
    val igv = subtotalGeneral * 0.18
    val totalGeneral = subtotalGeneral + igv

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                itemSeleccionado = itemSeleccionado,
                onItemSelected = { nuevoItem ->
                    itemSeleccionado = nuevoItem
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mi Carrito TECSUP") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF5C6BC0),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Formulario de ingreso
                OutlinedTextField(
                    value = nombreProducto,
                    onValueChange = { nombreProducto = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = precioProducto,
                        onValueChange = { precioProducto = it },
                        label = { Text("Precio (S/)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = cantidadProducto,
                        onValueChange = { cantidadProducto = it },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val p = precioProducto.toDoubleOrNull() ?: 0.0
                        val c = cantidadProducto.toIntOrNull() ?: 0
                        if (nombreProducto.isNotBlank() && p > 0 && c > 0) {
                            listaProductos.add(
                                ItemCarrito(
                                    id = System.currentTimeMillis().toInt(),
                                    nombre = nombreProducto,
                                    precio = p,
                                    cantidad = c
                                )
                            )
                            nombreProducto = ""
                            precioProducto = ""
                            cantidadProducto = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
                ) {
                    Text("AGREGAR", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de productos
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(listaProductos) { item ->
                        TarjetaProducto(
                            item = item,
                            onEliminar = { listaProductos.remove(item) }
                        )
                    }
                }

                // Resumen de precios
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Items: " + listaProductos.size.toString())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal")
                    Text("S/ " + subtotalGeneral.toString())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("IGV (18%)")
                    Text("S/ " + igv.toString())
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "S/ " + totalGeneral.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}