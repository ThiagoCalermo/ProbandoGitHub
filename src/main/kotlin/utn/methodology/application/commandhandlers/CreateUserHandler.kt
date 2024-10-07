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
    fun handle(command: CreateUserCommand):String {
        if (command.userName.isBlank() || command.email.isBlank()) {
            return "Error: El nombre de usuario y el correo electrónico no pueden estar vacíos.";
        }

        // Verificar si el usuario ya existe
        val existingUser = usuarioRepositorio.existenciaporEmail(command.email)
        if (existingUser) {
            return "Error: Ya existe un usuario con este correo electrónico."
        }


        val usuario = Usuario(
            uuid = UUID.randomUUID().toString(),
            name = command.name,
            userName = command.userName,
            email = command.email,
            password = command.password
        )


        return try {
            usuarioRepositorio.guardarOActualizar(usuario)
            return "Usuario creado exitosamente"
        } catch (e: Exception) {
            "Error al crear el usuario: ${e.message}"
        }
    }
} //comentario de alejo aguante dnd