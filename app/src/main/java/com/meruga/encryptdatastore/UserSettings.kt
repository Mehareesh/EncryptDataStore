package com.meruga.encryptdatastore

@kotlinx.serialization.Serializable
data class UserSettings(
    val username: String? = null,
    val password: String? = null
)
