package com.olano.lab04_carrito_olano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.olano.lab04_carrito_olano.screens.AppNavegacion
import com.olano.lab04_carrito_olano.ui.theme.Lab04carritoolanoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04carritoolanoTheme {
                AppNavegacion()
            }
        }
    }
}