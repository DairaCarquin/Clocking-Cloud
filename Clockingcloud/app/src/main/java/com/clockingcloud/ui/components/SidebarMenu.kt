package com.clockingcloud.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*

@Composable
fun SidebarMenu(
    userName: String,
    userRole: String,
    employeeCode: String,
    profilePhoto: String?,
    onItemClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f) // 🔹 sidebar más estrecho
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔹 SECCIÓN SUPERIOR: FOTO IZQUIERDA + DATOS DERECHA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FOTO
                if (profilePhoto != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profilePhoto),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userName.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                // DATOS A LA DERECHA
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = userName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userRole,
                            color = Color(0xFF9E9E9E),
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = employeeCode,
                            color = Color(0xFF9E9E9E),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 🔹 MENÚ DE OPCIONES (ocupa todo el largo restante)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val menuItems = listOf(
                    "Asistencia" to FontAwesomeIcons.Solid.UserClock,
                    "Calendario" to FontAwesomeIcons.Solid.CalendarAlt,
                    "Solicitudes" to FontAwesomeIcons.Solid.FileAlt,
                    "Visitas" to FontAwesomeIcons.Solid.UserFriends,
                    "Reporte de Visita" to FontAwesomeIcons.Solid.Bullhorn,
                    "Lenguaje" to FontAwesomeIcons.Solid.Globe,
                    "Configuración" to FontAwesomeIcons.Solid.Cog,
                    "Cerrar sesión" to FontAwesomeIcons.Solid.SignOutAlt
                )

                menuItems.forEach { (label, icon) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(label) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = Color(0xFF90CAF9),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(18.dp))
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}