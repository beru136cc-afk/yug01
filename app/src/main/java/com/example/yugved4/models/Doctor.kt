package com.example.yugved4.models

import java.util.UUID

/**
 * Data class for doctor information
 */
data class Doctor(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val specialty: String,
    val phoneNumber: String,
    val email: String? = null
)
