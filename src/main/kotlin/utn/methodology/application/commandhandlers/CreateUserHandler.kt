package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import java.util.UUID


//this.name = name
//        this.userName = userName
//        this.email = email
//        this.password = password
class CreateUserHandler(
    private val usuarioRepositorio : RepositorioUsuario
) {
    fun handle(command: CreateUserCommand) {
        val usuario = Usuario(
            uuid = UUID.randomUUID().toString(),
            name = command.name,
            userName = command.userName,
            email = command.email,
            password = command.password
        )
        usuarioRepositorio.GuardaroActualizar(usuario)
    }
} //comentario de alejo aguante dnd