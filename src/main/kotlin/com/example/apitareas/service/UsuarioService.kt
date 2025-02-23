package com.example.apitareas.service

import com.example.apitareas.dto.UsuarioDTO
import com.example.apitareas.dto.UsuarioInsertDTO
import com.example.apitareas.error.exception.BadRequestException
import com.example.apitareas.error.exception.NotFoundException
import com.example.apitareas.error.exception.UnauthorizedException
import com.example.apitareas.error.exception.UserExistException
import com.example.apitareas.model.Direccion
import com.example.apitareas.model.Usuario
import com.example.apitareas.repository.UsuarioRepository
import com.example.apitareas.utils.DTOMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService: UserDetailsService {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    @Autowired
    private lateinit var apiService: ExternalApiService

    override fun loadUserByUsername(username: String?): UserDetails {
        var usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username-+ no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertDTO: UsuarioInsertDTO): UsuarioDTO {
        val exist = usuarioRepository.findUserBy_id(usuarioInsertDTO.email)

        if (exist.isPresent) {
            throw UserExistException("Usuario existente.")
        }

        comprobarUser(usuarioInsertDTO)

        usuarioInsertDTO.password = passwordEncoder.encode(usuarioInsertDTO.password)

        val user = DTOMapper.userDTOToEntity(usuarioInsertDTO)

        usuarioRepository.insert(user)

        return DTOMapper.userEntityToDTO(user)
    }

    fun getUser(email: String): Usuario {

        return usuarioRepository.findUserBy_id(email).orElseThrow {
            NotFoundException("Usuario con email $email no encontrado.")
        }
    }

    fun getUserByUsername(username: String):Usuario{
        return usuarioRepository.findByUsername(username).orElseThrow {
            NotFoundException("Usuario $username no encontrado.")
        }
    }

    fun deleteUsuario(email: String): Usuario {
        val user = usuarioRepository.findUserBy_id(email).orElseThrow {
            NotFoundException("Usuario con email $email no encontrado.")
        }
        usuarioRepository.delete(user)
        return user
    }

    fun updateUsuario(usuario: UsuarioInsertDTO) : Usuario{
        val existUser = usuarioRepository.findUserBy_id(usuario.email).orElseThrow {
            NotFoundException("Usuario con email ${usuario.email} no encontrado.")
        }
        comprobarUser(usuario)

        usuario.password = passwordEncoder.encode(usuario.password)

        existUser.nombre = usuario.nombre
        existUser.password = usuario.password
        existUser.username = usuario.username
        existUser.roles = usuario.rol ?: "USER"
        existUser.direccion = Direccion(
            calle = usuario.calle,
            num = usuario.numero,
            municipio = usuario.municipio,
            provincia = usuario.provincia,
            cp = usuario.cp
        )
        return usuarioRepository.save(existUser)
    }

    private fun comprobarUser(user: UsuarioInsertDTO){
        if (user.email.isBlank() || !user.email.contains("@")) {
            throw BadRequestException("Formato de email inválido")
        }

        if (user.password.isBlank()) throw BadRequestException("Formato de password informado")
        if (user.nombre.isBlank()) throw BadRequestException("Formato de nombre informado")
        if (user.calle.isBlank()) throw BadRequestException("Formato de calle informado")
        if (user.numero.isBlank()) throw BadRequestException("Formato de numero informado")
        if (user.passwordRepeat.isBlank() || user.password != user.passwordRepeat) throw BadRequestException("Formato de password repeat informado")

        if (user.rol != "ADMIN" && user.rol != "USER" && user.rol != null) {
            throw BadRequestException("El rol introducido es inválido. Asegurese de especificar USER, ADMIN o dejelo en blanco (por defecto USER)")
        }

        val provinciaData = apiService.obtenerDatosProvincias()?.data?.find { it.PRO == user.provincia }
            ?: throw NotFoundException("Provincia ${user.provincia} no válida")

        val cpro = provinciaData.CPRO

        val municipioValido = apiService.obtenerDatosMunicipios(cpro)?.data
            ?.any { it.DMUN50 == user.municipio } ?: false

        if (!municipioValido) throw NotFoundException("Municipio ${user.provincia} no válido")


    }


}