package com.example.apitareas.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document

@Document("Usuario")
data class Usuario(
    @BsonId
    val _id : String?,
    var nombre: String,
    var username: String,
    var password: String,
    var roles: String = "USER",
    var direccion: Direccion
)