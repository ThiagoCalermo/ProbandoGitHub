package utn.methodology.infrastructure.http.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val uuid: String,
    var name: String,
    var userName: String,
    var email: String,
    var seguidos: List<String> = emptyList(),
    var seguidores: List<String> = emptyList()
)
