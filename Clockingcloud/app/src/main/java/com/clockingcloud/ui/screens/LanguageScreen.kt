package com.clockingcloud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Globe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(onMenuClick: () -> Unit) {
    var selectedLanguage by remember { mutableStateOf("Español") }

    val languages = listOf(
        Triple("English", "English", "en"),
        Triple("Español", "Spanish", "es"),
        Triple("Turkey", "Turkish", "tr")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Elegir Lenguaje",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            // 🔹 Lista de idiomas
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                languages.forEach { (language, subtext, code) ->
                    LanguageCard(
                        title = language,
                        subtitle = subtext,
                        isSelected = selectedLanguage == language,
                        onClick = { selectedLanguage = language }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // 🔹 Botón Continuar (no requiere callback externo)
            Button(
                onClick = { /* Aquí podrías guardar la preferencia del idioma */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Continuar", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun LanguageCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF1A73E8) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color(0xFF303030), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Globe,
                        contentDescription = null,
                        tint = if (isSelected) Color(0xFF1A73E8) else Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        title,
                        color = if (isSelected) Color(0xFF1A73E8) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        subtitle,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF1A73E8),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}