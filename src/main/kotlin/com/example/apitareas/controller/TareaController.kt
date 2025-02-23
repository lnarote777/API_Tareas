package com.example.apitareas.controller

import com.example.apitareas.dto.TareaDTO
import com.example.apitareas.error.exception.BadRequestException
import com.example.apitareas.error.exception.UnauthorizedException
import com.example.apitareas.model.Tarea
import com.example.apitareas.service.TareaService
import com.example.apitareas.service.UsuarioService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {
    @Autowired
    private lateinit var tareaService: TareaService

    @Autowired
    private lateinit var usuarioService: UsuarioService


    @PreAuthorize("hasRole('ADMIN') or #tareaDTO.username == authentication.name")
    @PostMapping("/crear")
    fun insert(
        @RequestBody tareaDTO: TareaDTO?,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Tarea> {
        if (tareaDTO == null) {
            throw BadRequestException("Debe introducir los campos de la tarea")
        }
        val tarea = tareaService.insert(tareaDTO)
        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/todas")
    fun getAll(httpRequest: HttpServletRequest): ResponseEntity<List<Tarea>> {
        val tareas = tareaService.getAll()
        return ResponseEntity(tareas, HttpStatus.OK)
    }


    @PreAuthorize("hasRole('USER') and #username == authentication.name")
    @GetMapping("/mis-tareas/{username}")
    fun getTareaByUser(
        @PathVariable username: String?,
        httpRequest: HttpServletRequest
    ): ResponseEntity<List<Tarea>> {
        if (username == null || username == "") {
            throw BadRequestException("Introduzca su nombre de usuario")
        }
        val usuario = usuarioService.getUserByUsername(username)

        val tareas = tareaService.getTareaByUser(usuario)

        return ResponseEntity(tareas, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('USER') and #tareaDTO.username == authentication.name")
    @PutMapping("/update/{id}")
    fun update(
        httpRequest: HttpServletRequest,
        @PathVariable id: Int
    ): ResponseEntity<Tarea> {

        val tarea = tareaService.update(id)

        return ResponseEntity(HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @DeleteMapping("/delete/{id}")
    fun delete(
        @PathVariable id: Int,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Any> {

        val username = httpRequest.userPrincipal.name
        val tarea = tareaService.getTarea(id)

        val usuario = usuarioService.getUserByUsername(username)

        if (usuario.roles == "ADMIN" || username == tarea.usuario.username) {
            val delete = tareaService.delete(id)
            return ResponseEntity(delete, HttpStatus.OK)
        }else{
            throw UnauthorizedException("El usuario $username no tiene permiso para eliminar la tarea del usuario ${tarea.usuario.username}")
        }

    }
}