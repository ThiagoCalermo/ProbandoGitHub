package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserCommand(
    val UserName: String,
    val Nombre: String,
    val Password: String,
    val email: String
) {
    fun validate(): CreateUserCommand {
        checkNotNull(Nombre) { throw IllegalArgumentException("Debe definir un nombre") }
        checkNotNull(UserName) { throw IllegalArgumentException("Debe definir un nombre de usuario") }
        checkNotNull(Password) { throw IllegalArgumentException("Debe definir una contraseña") }
        checkNotNull(email) { throw IllegalArgumentException("Debe definir un email") }

        return this
    }
}