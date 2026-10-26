package com.olano.lavacampus.ui.estudiante

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olano.lavacampus.model.Rol
import com.olano.lavacampus.ui.components.LavaButton
import com.olano.lavacampus.ui.components.LavaTextField
import com.olano.lavacampus.ui.theme.LavaTheme
import com.olano.lavacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExito: (Rol) -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("ana@correo.test") }
    var clave by remember { mutableStateOf("1234") }

    val error by vm.error.collectAsState()

    LavaTheme(rol = rolSeleccionado) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Ingresa para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Selector de rol (Estudiante / Administrador)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RolOptionButton(
                    texto = "Estudiante",
                    seleccionado = rolSeleccionado == Rol.CLIENTE,
                    onClick = {
                        rolSeleccionado = Rol.CLIENTE
                        correo = "ana@correo.test"
                        clave = "1234"
                        vm.limpiarError()
                    },
                    modifier = Modifier.weight(1f)
                )

                RolOptionButton(
                    texto = "Administrador",
                    seleccionado = rolSeleccionado == Rol.ADMIN,
                    onClick = {
                        rolSeleccionado = Rol.ADMIN
                        correo = "admin@lavacampus.test"
                        clave = "admin"
                        vm.limpiarError()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LavaTextField(
                valor = correo,
                onValorChange = {
                    correo = it
                    vm.limpiarError()
                },
                etiqueta = "Correo",
                teclado = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            LavaTextField(
                valor = clave,
                onValorChange = {
                    clave = it
                    vm.limpiarError()
                },
                etiqueta = "Contraseña",
                esClave = true
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LavaButton(
                texto = "Ingresar",
                onClick = { vm.login(correo, clave, rolSeleccionado, onLoginExito) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo práctica",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun RolOptionButton(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (seleccionado) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "bgColor"
    )
    val contentColor by animateColorAsState(
        if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = contentColor,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}