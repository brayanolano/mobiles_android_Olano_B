package com.olano.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StudentItem(
    val id: Int,
    val name: String,
    val career: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val studentList = listOf(
        StudentItem(1, "Brayan Olano", "Ingeniería de Sistemas"),
        StudentItem(2, "María García", "Arquitectura"),
        StudentItem(3, "Carlos Pérez", "Medicina"),
        StudentItem(4, "Ana López", "Derecho"),
        StudentItem(5, "Luis Ramírez", "Administración")
    )

    Scaffold(
        containerColor = Color(0xFFF6F3F9),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E35B1)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(studentList) { student ->
                StudentCard(
                    student = student,
                    onClick = { onItemClick(student.id) }
                )
            }
        }
    }
}

@Composable
private fun StudentCard(
    student: StudentItem,
    onClick: () -> Unit
) {
    val isPrimary = student.id == 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = if (isPrimary) Color(0xFFEDE7F6) else Color(0xFFF5F5F5),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = if (isPrimary) Color(0xFF5E35B1) else Color(0xFF757575),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.career,
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Detalles",
                tint = Color(0xFFBDBDBD)
            )
        }
    }
}
