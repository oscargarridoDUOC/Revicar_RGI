package com.example.revicar_rgi.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}
