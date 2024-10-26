package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.FollowUserCommand
import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

class FollowUserHandler (
    private val repositoriousuario : RepositorioUsuario
) {
    fun handle(command: FollowUserCommand):String {
        if (command.userId.isBlank() || command.userToFollowId.isBlank()) {
            return "Error: los id de ambos usuarios no pueden estar vacios no pueden estar vacíos.";
        }

        if (command.userId == command.userToFollowId) {
            return "Self-following not allowed."
        }

        // Verificar si el usuari existe
        if (!repositoriousuario.existsByUuid(command.userId)) {
            return "Error: debe existir el usuario."
        }

        if (!repositoriousuario.existsByUuid(command.userToFollowId)) {
            return "Error: debe existir el usuario a seguir."
        }

        return try {
            val user = repositoriousuario.RecuperarPorId(command.userId)
            val userToFollow = repositoriousuario.RecuperarPorId(command.userToFollowId)

            if (user == null || userToFollow == null) {
                return "usuarios no encontrados"
            }

            // Check user already following
            if (user.seguidos.contains(userToFollow.getId())) {
                // Unfollow the user
                unfollowUser(user, userToFollow)
            } else {
                // Follow the user
                followUser(user, userToFollow)
            }

            repositoriousuario.update(user)
            repositoriousuario.update(userToFollow)

            return "Usuario seguido exitosamente"
        } catch (e: Exception) {
            "Error al seguir el usuario: ${e.message}"
        }
    }

    private fun followUser(currentUser: Usuario, userToFollow: Usuario) {
        currentUser.agregarSeguido(userToFollow.getId())
        userToFollow.agregarSeguidor(currentUser.getId())
    }


    private fun unfollowUser(currentUser: Usuario, userToFollow: Usuario) {
        currentUser.quitarSeguido(userToFollow.getId())
        userToFollow.quitarSeguidor(currentUser.getId())
    }
}