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
import com.example.revicar_rgi.data.model.Inspection
import java.text.NumberFormat
import java.util.*

enum class UserType {
    BUYER,
    MECHANIC
}

@Composable
fun InspectionCard(
    inspection: Inspection,
    userType: UserType,
    mechanicName: String? = null,
    onCardClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    val (statusColor, buttonText, buttonColor) = when (inspection.status) {
        "PENDIENTE" -> Triple(Color(0xFFFFA500), "ACEPTAR TRABAJO", Color(0xFF4CAF50))
        "ASIGNADO" -> Triple(Color.Blue, "FINALIZAR", Color(0xFFFFA500))
        "FINALIZADO" -> Triple(Color.Gray, "VER INFORME", MaterialTheme.colorScheme.primary)
        else -> Triple(Color.Black, "", Color.Transparent)
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    currencyFormat.maximumFractionDigits = 0
    val formattedPrice = currencyFormat.format(inspection.servicePrice)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${inspection.make} ${inspection.model} (${inspection.year})",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${inspection.direccion}, ${inspection.comuna}",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = inspection.status,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = formattedPrice,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            if ((inspection.status == "ASIGNADO" || inspection.status == "FINALIZADO") && mechanicName != null) {
                Text(
                    text = "Mecánico: $mechanicName",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            when (userType) {
                UserType.BUYER -> {
                    if (inspection.status == "FINALIZADO") {
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { onButtonClick() }, modifier = Modifier.fillMaxWidth()) {
                            Text(buttonText)
                        }
                    }
                }
                UserType.MECHANIC -> {
                    if (inspection.status == "PENDIENTE" || inspection.status == "ASIGNADO") {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { onButtonClick() },
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