package com.example.apitareas.dto

data class TareaDTO (
    val id : Int? = null,
    val titulo:String,
    val descripcion:String,
    val estado:String? = null,
    val username: String
)