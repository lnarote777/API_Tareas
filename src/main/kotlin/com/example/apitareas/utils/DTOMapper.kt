package com.example.apitareas.utils

import com.example.apitareas.dto.TareaDTO
import com.example.apitareas.dto.UsuarioDTO
import com.example.apitareas.dto.UsuarioInsertDTO
import com.example.apitareas.model.Direccion
import com.example.apitareas.model.Tarea
import com.example.apitareas.model.Tarea.Companion.cont
import com.example.apitareas.model.Usuario
import java.util.concurrent.atomic.AtomicInteger

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
            _id = cont.getAndIncrement(),
            titulo = tareaDTO.titulo,
            descripcion = tareaDTO.descripcion,
            estado = "PENDIENTE",
            usuario = usuario
        )
    }

    fun tareaEntityToDTO(tareaEntity: Tarea): TareaDTO {
        return TareaDTO(
            id = tareaEntity._id,
            titulo = tareaEntity.titulo,
            descripcion = tareaEntity.descripcion,
            username = tareaEntity.usuario.username,
            estado = tareaEntity.estado
        )
    }
}