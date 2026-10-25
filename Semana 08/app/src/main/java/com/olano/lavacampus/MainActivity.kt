package com.olano.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.olano.lavacampus.model.Rol
import com.olano.lavacampus.ui.navigation.NavLava
import com.olano.lavacampus.ui.theme.LavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LavaTheme(rol = Rol.CLIENTE, modoOscuro = false) {
                NavLava()
            }
        }
    }
}