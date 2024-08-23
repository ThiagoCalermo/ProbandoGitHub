package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.entities.User
import utn.methodology.infrastructure.persistence.MongoUserRepository
import java.util.UUID

class CreateUserHandler(
    private val usuarioRepositorio : MongoUserRepository
) {
    fun handle(command: CreateUserCommand) {
        val usuario = User(
            UUID.randomUUID().toString(),
            command.name,
            command.email,
            command.password
        )
        usuarioRepositorio.save(usuario)
    }
} //comentario de alejo aguante dnd