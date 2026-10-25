package com.olano.lavacampus

import android.app.Application
import com.olano.lavacampus.data.local.DatosIniciales
import com.olano.lavacampus.data.local.LavaDatabase
import com.olano.lavacampus.data.repository.LavaRepository
import com.olano.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LavaApp : Application() {
    val database: LavaDatabase by lazy { LavaDatabase.obtener(this) }
    val repository: LavaRepository by lazy { LavaRepositoryRoom(database) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}
