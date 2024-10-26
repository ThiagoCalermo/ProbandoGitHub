package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.FollowUserHandler
import utn.methodology.application.commands.FollowUserCommand


data class FollowUserAction(
    private val handler: FollowUserHandler
) {

    fun execute(body: FollowUserCommand) {
        if (body.userId.isBlank() || body.userToFollowId.isBlank()) {
            throw IllegalArgumentException("Datos no válidos")
        }
        body.validate().let {
            handler.handle(it)
        }

    }
}
