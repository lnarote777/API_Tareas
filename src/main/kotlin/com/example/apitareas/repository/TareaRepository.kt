package com.example.apitareas.repository

import com.example.apitareas.model.Tarea
import com.example.apitareas.model.Usuario
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TareaRepository : MongoRepository<Tarea, Int> {
    fun findByUsuario(usuario: Usuario):List<Tarea>
}