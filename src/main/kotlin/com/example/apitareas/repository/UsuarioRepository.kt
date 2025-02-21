package com.example.apitareas.repository

import com.example.apitareas.model.Usuario
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsuarioRepository : MongoRepository<Usuario, String> {
    fun findUserBy_id(email: String): Optional<Usuario>
    fun findByUsername(username: String): Optional<Usuario>
}