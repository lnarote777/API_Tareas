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

    fun insert(tareaInsert: TareaDTO): TareaDTO{
        if (tareaInsert.titulo.isBlank()
            || tareaInsert.descripcion.isBlank()
            || tareaInsert.username.isBlank()){
            throw BadRequestException("Rellene todos los campos.")
        }

        val usuario = userService.getUserByUsername(tareaInsert.username)

        val tarea = DTOMapper.tareaDTOToEntity(tareaInsert, usuario)

        val tareaSaved = tareaRepository.insert(tarea)

        val tareaDTO = DTOMapper.tareaEntityToDTO(tareaSaved)

        return tareaDTO
    }

    fun getTareaByUser(usuario: Usuario): List<TareaDTO>{
        val tareasRepo = tareaRepository.findByUsuario(usuario)

        if (tareasRepo.isEmpty()){
            throw NotFoundException("No se encontró ninguna tarea para el usuario ${usuario.username}")
        }
        val tareas = mutableListOf<TareaDTO>()

        tareasRepo.forEach {
            val tarea = DTOMapper.tareaEntityToDTO(it)
            tareas.add(tarea)
        }

        return tareas
    }

    fun getTarea(id: Int): Tarea {
        return tareaRepository.findById(id)
            .orElseThrow {
                throw NotFoundException("Tarea no encontrada")
            }
    }

    fun getAll(): List<TareaDTO>{
        val tareasRepo = tareaRepository.findAll()

        if (tareasRepo.isEmpty()){
            throw NotFoundException("No se encontró ninguna tarea.")
        }

        val tareas = mutableListOf<TareaDTO>()

        tareasRepo.forEach {
            val tarea = DTOMapper.tareaEntityToDTO(it)
            tareas.add(tarea)
        }

        return tareas
    }

    fun update(tareaId: Int): TareaDTO{
        val tarea = tareaRepository.findById(tareaId).orElseThrow{
            throw NotFoundException("Tarea no encontrada")
        }

        if (tarea.estado == "COMPLETADA") throw BadRequestException("La tarea ya ha sido completada.")

        tarea._id = tareaId
        tarea.estado = "COMPLETADA"

        val tareaSaved = tareaRepository.save(tarea)

        return DTOMapper.tareaEntityToDTO(tareaSaved)
    }

    fun delete(tareaId: Int):TareaDTO{
        val tarea = tareaRepository.findById(tareaId).orElseThrow{
            throw NotFoundException("Tarea no encontrada")
        }

        tarea._id = tareaId

        tareaRepository.delete(tarea)
        return DTOMapper.tareaEntityToDTO(tarea)
    }
}