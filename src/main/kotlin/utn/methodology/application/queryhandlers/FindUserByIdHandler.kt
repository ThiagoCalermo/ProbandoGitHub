package utn.methodology.application.queryhandlers

//import utn.methodology.application.queries.FindUserByIdQuery   FALTA
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import io.ktor.server.plugins.*

class FindUserByIdHandler(
    private val usuarioRepositorio: MongoUserRepository
) {

    fun handle(query: FindUserByIdQuery): Map<String, String> {

        val usuario = usuarioRepositorio.findOne(query.id)

        if (usuario == null) {
            throw NotFoundException("Id de usuario: ${query.id} no encontrado")
        }

        return usuario.toPrimitives()
    }
} //comentario de alejooo