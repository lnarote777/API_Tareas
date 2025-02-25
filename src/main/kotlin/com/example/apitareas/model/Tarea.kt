package com.example.apitareas.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.concurrent.atomic.AtomicInteger

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
        val cont = AtomicInteger(0)  // El contador es compartido por todas las instancias
    }
}
