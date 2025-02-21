package com.example.apitareas.dto

data class UsuarioInsertDTO(
    val username: String,
    val nombre : String,
    val email: String,
    var password: String,
    val passwordRepeat: String,
    val rol: String?,
    val calle: String,
    val numero: String,
    val municipio: String,
    val provincia: String,
    val cp : String
)
