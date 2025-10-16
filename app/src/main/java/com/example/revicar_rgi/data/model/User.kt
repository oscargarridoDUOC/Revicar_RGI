package com.example.revicar_rgi.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val isMechanic: Boolean = false,
    val name: String = "",
    val lastName: String = "",
    val run: String = "",
    val phone: String = ""
)