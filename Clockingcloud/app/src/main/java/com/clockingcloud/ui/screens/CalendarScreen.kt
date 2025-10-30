package com.clockingcloud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(onMenuClick: () -> Unit) {
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }

    var selectedMonth by remember { mutableStateOf(today.monthValue) }
    var selectedYear by remember { mutableStateOf(today.year) }
    var expanded by remember { mutableStateOf(false) }

    val localeEs = Locale.forLanguageTag("es")

    val backgroundColor = Color.White
    val primaryColor = Color(0xFF5B6BF8)
    val textColor = Color(0xFF121212)
    val grayText = Color(0xFF9E9E9E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Calendario",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Selector de mes y año similar al módulo de Asistencia
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .clickable { expanded = true }
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${Month.of(selectedMonth).getDisplayName(TextStyle.FULL, localeEs)} $selectedYear",
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar mes", tint = textColor)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    (1..12).forEach { month ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    Month.of(month).getDisplayName(TextStyle.FULL, localeEs)
                                        .replaceFirstChar { it.uppercase() }
                                )
                            },
                            onClick = {
                                selectedMonth = month
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // 🔹 Encabezado de días de la semana
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach {
                    Text(
                        it,
                        color = grayText,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 🔹 Generar datos del calendario
            val firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1)
            val daysInMonthCount = firstDayOfMonth.lengthOfMonth()
            val startOffset = (firstDayOfMonth.dayOfWeek.value % 7)
            val totalCells = startOffset + daysInMonthCount

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Celdas vacías antes del día 1
                items(startOffset) {
                    Box(modifier = Modifier.size(40.dp))
                }

                // Días del mes
                items(daysInMonthCount) { index ->
                    val date = LocalDate.of(selectedYear, selectedMonth, index + 1)
                    val isSelected = date == selectedDate
                    val isToday = date == today

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> primaryColor
                                    isToday -> Color(0xFFE3E3E3)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable { selectedDate = date },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isSelected) Color.White else textColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🔹 Información del día seleccionado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "${selectedDate.dayOfMonth} ${
                        selectedDate.month.getDisplayName(TextStyle.FULL, localeEs)
                    } ${selectedDate.year}",
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    fontSize = 16.sp
                )

                Row {
                    Text(
                        text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, localeEs),
                        color = Color(0xFF5B8DF8),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = " - Horario clásico",
                        color = grayText,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Calendario programado: 10:30 AM - 06:00 PM",
                    color = grayText,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(30.dp))

            // 🔹 Botón de acción
            Button(
                onClick = { /* Acción futura */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Solicitar cambio de calendario", fontSize = 15.sp)
            }
        }
    }
}
