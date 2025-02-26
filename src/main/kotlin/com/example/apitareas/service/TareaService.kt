package com.example.apitareas.service

import com.example.apitareas.dto.TareaDTO
import com.example.apitareas.dto.UsuarioDTO
import com.example.apitareas.error.exception.BadRequestException
import com.example.apitareas.error.exception.NotFoundException
import com.example.apitareas.model.Tarea
import com.example.apitareas.model.Usuario
import com.example.apitareas.repository.TareaRepository
import com.example.apitareas.repository.UsuarioRepository
import com.example.apitareas.utils.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TareaService {
    @Autowired
    private lateinit var tareaRepository: TareaRepository

    @Autowired
    private lateinit var userService: UsuarioService

    fun insert(tareaInsert: TareaDTO): Tarea{
        if (tareaInsert.titulo.isBlank()
            || tareaInsert.descripcion.isBlank()
            || tareaInsert.username.isBlank()){
            throw BadRequestException("Rellene todos los campos.")
        }

        val usuario = userService.getUserByUsername(tareaInsert.username)

        val tarea = DTOMapper.tareaDTOToEntity(tareaInsert, usuario)
        return tareaRepository.insert(tarea)
    }

    fun getTareaByUser(usuario: Usuario): List<Tarea>{
        val tareas = tareaRepository.findByUsuario(usuario)

        if (tareas.isEmpty()){
            throw NotFoundException("No se encontró ninguna tarea para el usuario ${usuario.username}")
        }

        return tareas
    }

    fun getTarea(id: Int): Tarea {
        return tareaRepository.findById(id)
            .orElseThrow {
                throw NotFoundException("Tarea no encontrada")
            }
    }

    fun getAll(): List<Tarea>{
        val tareas = tareaRepository.findAll()

        if (tareas.isEmpty()){
            throw NotFoundException("No se encontró ninguna tarea.")
        }

        return tareas
    }

    fun update(tareaId: Int): Tarea{
        val tarea = tareaRepository.findById(tareaId).orElseThrow{
            throw NotFoundException("Tarea no encontrada")
        }


        tarea._id = tareaId
        tarea.estado = "COMPLETADA"

        return tareaRepository.save(tarea)
    }

    fun delete(tareaId: Int):Tarea{
        val tarea = tareaRepository.findById(tareaId).orElseThrow{
            throw NotFoundException("Tarea no encontrada")
        }

        tarea._id = tareaId

        tareaRepository.delete(tarea)
        return tarea
    }
}