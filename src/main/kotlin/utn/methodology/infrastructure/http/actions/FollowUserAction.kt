package utn.methodology.infrastructure.http.actions


data class FollowUserAction(
    val userId: String,
    val userToFollowId: String
)
