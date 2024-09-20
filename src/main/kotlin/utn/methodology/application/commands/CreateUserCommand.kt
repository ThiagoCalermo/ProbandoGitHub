package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserCommand(
    val userName: String,
    val name: String,
    val password: String,
    val email: String
) {
    fun validate(): CreateUserCommand {
        checkNotNull(name) { throw IllegalArgumentException("Debe definir un nombre") }
        checkNotNull(userName) { throw IllegalArgumentException("Debe definir un nombre de usuario") }
        checkNotNull(password) { throw IllegalArgumentException("Debe definir una contraseña") }
        checkNotNull(email) { throw IllegalArgumentException("Debe definir un email") }

        return this
    }
}