package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.commands.CreateUserCommand

class CreateUserAction(
    private val handler: CreateUserHandler
) {

    fun execute(body: CreateUserCommand) {
        if (body.userName.isBlank() || body.email.isBlank() || body.password.isBlank()) {
            throw IllegalArgumentException("Datos no válidos")
        }
        body.validate().let {
            handler.handle(it)
        }

    }
}