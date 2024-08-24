package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.entities.Usuario
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import java.util.UUID

class CreateUserHandler(
    //private val usuarioRepositorio : MongoUserRepository
) {
    fun handle(command: CreateUserCommand) {
        val usuario = Usuario(
            UUI.randomUUID().toString(),
            command.Nombre,
            command.email,
            command.Password
        )
        usuarioRepositorio.save(usuario)
    }
} //comentario de alejo aguante dnd