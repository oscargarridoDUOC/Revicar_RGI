package com.example.revicar_rgi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revicar_rgi.ui.theme.*

enum class UserType {
    BUYER,
    MECHANIC
}

@Composable
fun InspectionCard(
    modifier: Modifier = Modifier,
    userType: UserType,
    status: String,
    onCardClick: () -> Unit
) {
    val (statusColor, buttonText, buttonColor) = when (status) {
        "PENDIENTE" -> Triple(Orange, "ACEPTAR TRABAJO", Green)
        "ASIGNADO" -> Triple(Blue, "FINALIZAR", Orange)
        "FINALIZADO" -> Triple(Color.Gray, "VER INFORME", MaterialTheme.colorScheme.primary)
        else -> Triple(Color.Black, "", Color.Transparent)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kia Morning 2018",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = status,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Text(
                text = "Dirección completa en Maipú",
                fontSize = 16.sp
            )
            Text(
                text = "$90.000",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (status == "ASIGNADO" || status == "FINALIZADO") {
                Text(
                    text = "Mecánico: Ricardo Illanes",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            when (userType) {
                UserType.BUYER -> {
                    if (status == "FINALIZADO") {
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { /* Lógica */ }, modifier = Modifier.fillMaxWidth()) {
                            Text(buttonText)
                        }
                    }
                }
                UserType.MECHANIC -> {
                    if (status == "PENDIENTE" || status == "ASIGNADO") {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { /* Lógica */ },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(buttonText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InspectionCardBuyerPreview() {
    Revicar_RGITheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("VISTA COMPRADOR", style = MaterialTheme.typography.headlineSmall)
            InspectionCard(userType = UserType.BUYER, status = "PENDIENTE", onCardClick = {})
            InspectionCard(userType = UserType.BUYER, status = "ASIGNADO", onCardClick = {})
            InspectionCard(userType = UserType.BUYER, status = "FINALIZADO", onCardClick = {})
        }
    }
}

@Composable
fun InspectionCardMechanicPreview() {
    Revicar_RGITheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("VISTA MECÁNICO", style = MaterialTheme.typography.headlineSmall)
            InspectionCard(userType = UserType.MECHANIC, status = "PENDIENTE", onCardClick = {})
            InspectionCard(userType = UserType.MECHANIC, status = "ASIGNADO", onCardClick = {})
            InspectionCard(userType = UserType.MECHANIC, status = "FINALIZADO", onCardClick = {})
        }
    }
}