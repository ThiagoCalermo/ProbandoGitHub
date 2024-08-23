package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserCommand(
    private val Id: String,
    private var Nombre: String,
    private var Password: String,
    private var email: String
) {
    fun validate(): CreateUserCommand {
        checkNotNull(Nombre) { throw IllegalArgumentException("Debe definir un nombre") }
        checkNotNull(Password) { throw IllegalArgumentException("Debe definir una contraseña") }
        checkNotNull(email) { throw IllegalArgumentException("Debe definir un email") }

        return this
    }
}