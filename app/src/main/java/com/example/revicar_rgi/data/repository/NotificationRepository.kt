package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.Notification

class NotificationRepository {
    fun getMyNotifications(): List<Notification> {
        return listOf(
            Notification(1, "Mecánico", "No se ha podido contactar con el mecánico"),
            Notification(2, "Usuario", "Tus datos han sido verificados"),
            Notification(3, "Actualización", "El mecánico se encuentra en camino al vehículo")
        )
    }
}