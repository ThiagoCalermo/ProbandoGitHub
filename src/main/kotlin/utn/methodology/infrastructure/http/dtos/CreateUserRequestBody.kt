package utn.methodology.infrastructure.http.dtos
import kotlinx.serialization.Serializable

@Serializable
class CreateUserRequestBody (
    var name: String = "",
    var userName: String = "",
    var email: String="",
    var seguidos: List<String> = emptyList(),
    var seguidores: List<String> = emptyList()
) {
    fun validate(): CreateUserRequestBody {
        checkNotNull(name) { throw IllegalArgumentException("El nombre debe ser definido") }
        checkNotNull(userName) { throw IllegalArgumentException("El Username debe ser definido ") }
        checkNotNull(email) { throw IllegalArgumentException("El Email debe ser definido") }

        // esta parte es opcional porque una persona puede no seguir a nadie y puede no tener seguidores
        if (seguidos.any { it.isEmpty() }) {
            throw IllegalArgumentException("La lista de seguidos no puede contener valores vacíos")
        }
        if (seguidores.any { it.isEmpty() }) {
            throw IllegalArgumentException("La lista de seguidores no puede contener valores vacíos")
        }
        return this;
    }

}