package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
data class FollowUserCommand (
    val userId: String,
    val userToFollowId: String
) {
    fun validate(): FollowUserCommand {
        checkNotNull(userId) { throw IllegalArgumentException("debe incluir un usuario") }
        checkNotNull(userToFollowId) { throw IllegalArgumentException("debe incluir un usuario a seguir") }

        return this
    }
}