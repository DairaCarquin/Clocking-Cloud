package com.clockingcloud.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.clockingcloud.data.remote.ApiClient
import com.clockingcloud.data.remote.AttendanceApi
import com.clockingcloud.data.remote.UserApi
import com.clockingcloud.ui.components.HomeContent
import com.clockingcloud.ui.components.SidebarMenu
import com.clockingcloud.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null)  {
    val context = LocalContext.current
    val session = SessionManager(context)
    val apiAttendance = ApiClient.getInstance(context).create(AttendanceApi::class.java)
    val apiUser = ApiClient.getInstance(context).create(UserApi::class.java)
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentScreen by remember { mutableStateOf("home") }

    var events by remember {
        mutableStateOf(
            mapOf(
                "Entrada" to "--:--:--",
                "Salida" to "--:--:--",
                "Horas trabajadas" to "--:--:--"
            )
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf("Cargando...") }
    var userRole by remember { mutableStateOf("") }
    var employeeCode by remember { mutableStateOf("") }
    var profilePhoto by remember { mutableStateOf<String?>(null) }

    fun loadUserData(userId: Long) {
        scope.launch {
            withContext(Dispatchers.IO) { // 👈 ejecuta fuera del hilo principal
                try {
                    val response = apiUser.getUserById(userId)
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        withContext(Dispatchers.Main) {
                            userName = user.name
                            userRole = user.role
                            profilePhoto = user.profilePhoto
                            val initials = user.name.take(3).uppercase()
                            employeeCode = initials + user.id.toString().padStart(2, '0')
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            userName = "Usuario desconocido"
                            userRole = "Sin rol"
                            employeeCode = "--"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        userName = "Error al cargar"
                        userRole = "Sin rol"
                        employeeCode = "--"
                    }
                }
            }
        }
    }

    fun refreshEvents(userId: Long) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = apiAttendance.getLastEvent(userId.toInt())
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        withContext(Dispatchers.Main) {
                            events = mapOf(
                                "Entrada" to (data.entrada ?: "--:--:--"),
                                "Salida" to (data.salida ?: "--:--:--"),
                                "Horas trabajadas" to (data.horasTrabajadas ?: "--:--:--")
                            )
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    }

    val userId = 1L

    /*LaunchedEffect(Unit) {
        loadUserData(userId)
        refreshEvents(userId)
    }*/
    LaunchedEffect(Unit) {
        loadUserData(userId)
        kotlinx.coroutines.delay(300)
        refreshEvents(userId)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarMenu(
                userName = userName,
                userRole = userRole,
                employeeCode = employeeCode,
                profilePhoto = profilePhoto,
                onItemClick = { item ->
                    scope.launch {
                        drawerState.close()
                        kotlinx.coroutines.delay(180)
                        currentScreen = when (item) {
                            "Asistencia" -> "attendance"
                            "Calendario" -> "calendar"
                            "Solicitudes" -> "solicitudes"
                            "Visitas" -> "visitas"
                            "Reporte de Visita" -> "reporte"
                            "Lenguaje" -> "lenguaje"
                            "Configuración" -> "configuración"
                            "Cerrar sesión" -> {
                                session.clearSession()
                                navController?.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                                "home"
                            }
                            else -> "home"
                        }
                    }
                }
            )
        }
    ) {
        when (currentScreen) {
            "home" -> {
                HomeContent(
                    drawerState = drawerState,
                    scope = scope,
                    apiAttendance = apiAttendance,
                    userId = userId,
                    events = events,
                    showBottomSheet = showBottomSheet,
                    onShowBottomSheet = { showBottomSheet = it },
                    refreshEvents = { refreshEvents(userId) }
                )
            }

            "attendance" -> {
                AttendanceScreen (
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

            "calendar" -> {
                CalendarScreen(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

            "solicitudes" -> {
                SolicitudesScreen (
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

            "visitas" -> {
                VisitasScreen(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    },
                    onReporteClick = {
                        currentScreen = "reporte"
                    }
                )
            }

            "reporte" -> {
                ReporteVisitaScreen (
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

            "lenguaje" -> {
                LanguageScreen (
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

            "configuración" -> {
                ConfiguracionScreen (
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                )
            }

        }
    }

    if (showBottomSheet) {
        val entradaRegistrada =
            events["Entrada"]?.contains(":") == true && events["Entrada"] != "--:--:--"
        val salidaRegistrada =
            events["Salida"]?.contains(":") == true && events["Salida"] != "--:--:--"

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.White,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text("Registrar evento", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(24.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                apiAttendance.checkIn(userId.toInt())
                                refreshEvents(userId)
                            }
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        enabled = !entradaRegistrada,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                    ) { Text("Entrada") }

                    Button(
                        onClick = {
                            scope.launch {
                                apiAttendance.checkOut(userId.toInt())
                                refreshEvents(userId)
                            }
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        enabled = entradaRegistrada && !salidaRegistrada,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                    ) { Text("Salida") }
                }
            }
        }
    }
}