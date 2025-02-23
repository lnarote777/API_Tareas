package com.example.apitareas.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document

@Document("Tareas")
data class Tarea(
    @BsonId
    var _id: Int? = null,
    val titulo: String,
    val descripcion: String,
    var estado: String, // TERMINADA/PENDIENTE
    val usuario: Usuario
){
    companion object {
        private var cont = 0  // El contador es compartido por todas las instancias
    }
    init {
        _id = ++cont
    }
}
