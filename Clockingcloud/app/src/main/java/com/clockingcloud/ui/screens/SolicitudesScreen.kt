package com.clockingcloud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.FolderOpen
import compose.icons.fontawesomeicons.solid.Frown
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Filter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudesScreen(onMenuClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Solicitudes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de filtro futura */ }) {
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.Filter,
                            contentDescription = "Filtrar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Crear nueva solicitud */ },
                containerColor = Color(0xFF1A73E8),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Plus,
                    contentDescription = "Nueva solicitud",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.FolderOpen,
                        contentDescription = "Carpeta vacía",
                        modifier = Modifier.size(100.dp),
                        tint = Color(0xFFBDBDBD)
                    )
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Frown,
                        contentDescription = "Carita triste",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-12).dp, y = (-10).dp),
                        tint = Color(0xFF9E9E9E)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "No hay datos para visualizar",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}