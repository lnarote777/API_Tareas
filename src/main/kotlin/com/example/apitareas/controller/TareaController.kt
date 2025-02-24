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
        val usernameActual = httpRequest.userPrincipal.name
        val usuario = usuarioService.getUserByUsername(usernameActual)

        if (usernameActual != tareaDTO.username && usuario.roles != "ADMIN" ) {
            throw UnauthorizedException("No tiene permiso para añadirle una tarea a otro usuario")
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
        val userActual = httpRequest.userPrincipal.name

        if (userActual != username){
            throw UnauthorizedException("No tiene permiso para añadirle una tarea a otro usuario")
        }

        val usuario = usuarioService.getUserByUsername(username)

        val tareas = tareaService.getTareaByUser(usuario)

        return ResponseEntity(tareas, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('USER') and #tareaUpdate.username == authentication.name")
    @PutMapping("/update/{id}")
    fun update(
        httpRequest: HttpServletRequest,
        @PathVariable id:  String
    ): ResponseEntity<Tarea> {
        val idInt = try {
            id.toInt()
        }catch (e: Exception){
            throw BadRequestException("Inserte un id válido")
        }

        val usernameActual = httpRequest.userPrincipal.name
        val tareaUpdate = tareaService.getTarea(idInt)

        if (usernameActual != tareaUpdate.usuario.username ) {
            throw UnauthorizedException("No tiene permiso para añadirle una tarea a otro usuario")
        }

        val tarea = tareaService.update(idInt)

        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @DeleteMapping("/delete/{id}")
    fun delete(
        @PathVariable id: String,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Tarea> {

        val idInt = try {
            id.toInt()
        }catch (e: Exception){
            throw BadRequestException("Inserte un id válido")
        }

        val username = httpRequest.userPrincipal.name
        val tarea = tareaService.getTarea(idInt)

        val usuario = usuarioService.getUserByUsername(username)

        if (usuario.roles == "ADMIN" || username == tarea.usuario.username) {
            val delete = tareaService.delete(idInt)
            return ResponseEntity(delete, HttpStatus.OK)
        }else{
            throw UnauthorizedException("El usuario $username no tiene permiso para eliminar la tarea del usuario ${tarea.usuario.username}")
        }

    }
}