// ruta: ui/screens/buyer/InspectionDetailScreen.kt

package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revicar_rgi.ui.components.UserType
import com.example.revicar_rgi.ui.theme.*

@Composable
fun InspectionDetailContent(
    userType: UserType,
    status: String
) {
    val (statusColor, buttonText, buttonColor) = when (status) {
        "PENDIENTE" -> Triple(Orange, "ACEPTAR TRABAJO", Green)
        "ASIGNADO" -> Triple(Blue, "FINALIZAR", Orange)
        "FINALIZADO" -> Triple(Color.Gray, "VER INFORME", MaterialTheme.colorScheme.primary)
        else -> Triple(Color.Black, "", Color.Transparent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Kia Morning 2018", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Text(text = "Dirección completa en Maipú", fontSize = 16.sp, color = Color.Gray)

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Text(text = "Detalles", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(
            text = "Este es un texto de ejemplo largo que describe los detalles de la solicitud de inspección. Aquí iría la información adicional que el comprador haya ingresado.",
            fontSize = 16.sp
        )

        Text(text = "Servicios", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "• Servicio 1", fontSize = 16.sp)
            Text(text = "• Servicio 2", fontSize = 16.sp)
            Text(text = "• Servicio 3", fontSize = 16.sp)
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Estado:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = status,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Costo Total:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "$90.000",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (status == "ASIGNADO" || status == "FINALIZADO") {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Text("Información del Mecánico", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Nombre: Ricardo Illanes", fontSize = 16.sp)
            Text("Contacto: +56 9 8765 4321", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        when (userType) {
            UserType.BUYER -> {
                if (status == "FINALIZADO") {
                    Button(onClick = { /* Lógica */ }, modifier = Modifier.fillMaxWidth()) {
                        Text(buttonText)
                    }
                }
            }
            UserType.MECHANIC -> {
                if (status == "PENDIENTE" || status == "ASIGNADO") {
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

@Preview(showBackground = true, name = "Detalle Mecánico PENDIENTE")
@Composable
fun InspectionDetailPreviewMechanicPending() {
    Revicar_RGITheme {
        InspectionDetailContent(userType = UserType.MECHANIC, status = "PENDIENTE")
    }
}

@Preview(showBackground = true, name = "Detalle Comprador FINALIZADO")
@Composable
fun InspectionDetailPreviewBuyerFinished() {
    Revicar_RGITheme {
        InspectionDetailContent(userType = UserType.BUYER, status = "FINALIZADO")
    }
}