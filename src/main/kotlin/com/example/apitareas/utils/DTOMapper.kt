package com.example.apitareas.utils

import com.example.apitareas.dto.TareaDTO
import com.example.apitareas.dto.UsuarioDTO
import com.example.apitareas.dto.UsuarioInsertDTO
import com.example.apitareas.model.Direccion
import com.example.apitareas.model.Tarea
import com.example.apitareas.model.Usuario

object DTOMapper {

    fun userDTOToEntity(usuarioInsertDTO: UsuarioInsertDTO) : Usuario {
        return Usuario(
            nombre = usuarioInsertDTO.nombre,
            username = usuarioInsertDTO.username,
            _id = usuarioInsertDTO.email,
            password = usuarioInsertDTO.password,
            roles = usuarioInsertDTO.rol ?: "USER",
            direccion = Direccion(
                calle = usuarioInsertDTO.calle,
                num = usuarioInsertDTO.numero,
                municipio = usuarioInsertDTO.municipio,
                provincia = usuarioInsertDTO.provincia,
                cp = usuarioInsertDTO.cp
            )
        )
    }

    fun userEntityToDTO(usuario: Usuario) : UsuarioDTO {

        return UsuarioDTO(
            username = usuario.username,
            nombre = usuario.nombre,
            email = usuario._id,
            rol = usuario.roles
        )

    }

    fun tareaDTOToEntity(tareaDTO: TareaDTO, usuario: Usuario): Tarea{
        return Tarea(
            _id = null,
            titulo = tareaDTO.titulo,
            descripcion = tareaDTO.descripcion,
            estado = "PENDIENTE",
            usuario = usuario
        )
    }
}