package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostCommand(
    val userId: String,
    val message: String,

) {
    fun validate(): CreatePostCommand {
        if (message.length > 280) {
            throw IllegalArgumentException("El mensaje no puede superar los 280 caracteres.")
        }

        return this
    }

}