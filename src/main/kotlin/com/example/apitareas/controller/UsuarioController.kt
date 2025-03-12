package com.example.apitareas.controller


import com.example.apitareas.dto.UsuarioDTO
import com.example.apitareas.dto.UsuarioLoginDTO
import com.example.apitareas.dto.UsuarioInsertDTO
import com.example.apitareas.error.exception.BadRequestException
import com.example.apitareas.error.exception.UnauthorizedException
import com.example.apitareas.service.TokenService
import com.example.apitareas.service.UsuarioService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var tokenService: TokenService
    @Autowired
    private lateinit var usuarioService: UsuarioService

    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioInsertDTO: UsuarioInsertDTO
    ) : ResponseEntity<UsuarioDTO>?{

        val user = usuarioService.insertUser(usuarioInsertDTO)

        return ResponseEntity(user, HttpStatus.CREATED)

    }

    @PostMapping("/login")
    fun login(@RequestBody usuario: UsuarioLoginDTO?) : ResponseEntity<Any>? {
        if(usuario == null){
            throw BadRequestException("El parámetro no puede estar vacío.")
        }

        val authentication: Authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(usuario.username, usuario.password)
            )
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales incorrectas")
        }

        val token = tokenService.generarToken(authentication)
        return ResponseEntity(mapOf("token" to token), HttpStatus.OK)
    }

    @GetMapping("/lista-usuarios")
    fun getAllUsers(
        httpRequest: HttpServletRequest
    ): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioService.getAll()
        return ResponseEntity(usuarios, HttpStatus.OK)
    }

    @DeleteMapping("/delete/{email}")
    fun deleteByEmail(
        @PathVariable email: String,
        httpRequest: HttpServletRequest
    ) : ResponseEntity<UsuarioDTO>? {

        return ResponseEntity(null, HttpStatus.NO_CONTENT)
    }

    @PutMapping("/update")
    fun update(
        @RequestBody usuarioUpdated: UsuarioInsertDTO,
        httpRequest: HttpServletRequest,
    ): ResponseEntity<UsuarioDTO>{
        return ResponseEntity(null, HttpStatus.NO_CONTENT)
    }
}