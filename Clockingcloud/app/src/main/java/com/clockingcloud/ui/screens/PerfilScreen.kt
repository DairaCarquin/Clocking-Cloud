package com.clockingcloud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import compose.icons.fontawesomeicons.solid.UserCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(onMenuClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.UserCircle,
                contentDescription = "Perfil",
                modifier = Modifier.size(120.dp),
                tint = Color(0xFF616161)
            )
            Spacer(Modifier.height(10.dp))
            Text("Daira Carquin", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Software Development", color = Color(0xFF1A73E8), fontSize = 14.sp)

            Spacer(Modifier.height(20.dp))

            ProfileItem("Departamento", "App Dev")
            ProfileItem("Área", "Lima")
            ProfileItem("Correo", "daira.carquin@gmail.com")
            ProfileItem("Teléfono", "+51-910470878")

            Spacer(Modifier.height(20.dp))
            ProfileItem("Desarrollo", ">", isClickable = true)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, isClickable: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            Text(value, color = Color.LightGray, fontSize = 15.sp)
        }
    }
}