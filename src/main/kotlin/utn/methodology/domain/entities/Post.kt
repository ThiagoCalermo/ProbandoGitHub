package utn.methodology.domain.entities

class Post(
    val uuid: Int,
    val contenido: String,
    val fechaCreacion: String,
    val autorId: Int,
    val autorNombre: String
)

}