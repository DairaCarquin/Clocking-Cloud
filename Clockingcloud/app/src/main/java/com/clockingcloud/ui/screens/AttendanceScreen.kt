package com.clockingcloud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clockingcloud.data.remote.ApiClient
import com.clockingcloud.data.remote.AttendanceApi
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val api = remember { ApiClient.getInstance(context).create(AttendanceApi::class.java) }
    val scope = rememberCoroutineScope()

    var selectedMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }
    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val localeEs = Locale.forLanguageTag("es-ES")

    val dayStatuses = remember {
        mutableStateMapOf<LocalDate, String>().apply {
            put(LocalDate.of(2025, 10, 1), "A tiempo")
            put(LocalDate.of(2025, 10, 3), "Ausencia")
            put(LocalDate.of(2025, 10, 8), "Festivo")
            put(LocalDate.of(2025, 10, 15), "Llegada tarde")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Asistencia",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Selector de mes y año con DropdownMenu
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
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar mes", tint = Color.Black)
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

            // 🔹 Encabezado de días
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach {
                    Text(
                        it,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // 🔹 Cuerpo del calendario
            val firstDay = LocalDate.of(selectedYear, selectedMonth, 1)
            val lastDay = firstDay.lengthOfMonth()
            val startOffset = firstDay.dayOfWeek.ordinal
            val totalCells = startOffset + lastDay
            val weeks = ((startOffset + lastDay) + 6) / 7

            Column {
                var day = 1
                repeat(weeks) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        repeat(7) { col ->
                            val index = it * 7 + col
                            if (index < startOffset || day > lastDay) {
                                Box(Modifier.weight(1f).aspectRatio(1f)) {}
                            } else {
                                val date = LocalDate.of(selectedYear, selectedMonth, day)
                                val status = dayStatuses[date]
                                val color = when (status) {
                                    "A tiempo" -> Color(0xFF4CAF50)
                                    "Llegada tarde" -> Color(0xFFFFA726)
                                    "Ausencia" -> Color(0xFFEF5350)
                                    "Festivo" -> Color(0xFF7E57C2)
                                    else -> Color(0xFFE0E0E0)
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .background(color, RoundedCornerShape(50))
                                        .clickable { selectedDate = date },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        day.toString(),
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                day++
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🔹 Leyenda
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(Color(0xFF4CAF50), "A tiempo")
                LegendItem(Color(0xFFFFA726), "Tarde")
                LegendItem(Color(0xFFEF5350), "Ausente")
                LegendItem(Color(0xFF7E57C2), "Festivo")
            }

            Spacer(Modifier.height(20.dp))

            // 🔹 Card con detalle del día seleccionado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = selectedDate.toString(),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Entrada: 09:00:00 AM", color = Color.Gray, fontSize = 13.sp)
                    Text("Salida: 06:02:00 PM", color = Color.Gray, fontSize = 13.sp)
                    Text("Horas trabajadas: 07:30:00 Hrs", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(3.dp))
        )
        Spacer(Modifier.width(6.dp))
        Text(text, color = Color.Gray, fontSize = 12.sp)
    }
}