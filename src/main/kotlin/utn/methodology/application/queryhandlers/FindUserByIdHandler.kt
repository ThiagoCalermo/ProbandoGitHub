package utn.methodology.application.queryhandlers

import utn.methodology.application.queries.FindUserByIdQuery
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindUserByUsernameQuery

class FindUserByIdHandler(
    //private val usuarioRepositorio: MongoUserRepository
) {

    fun handle(query: FindUserByUsernameQuery): Map<String, String> {

        val usuario = repositorioUsuario.findOne(query.id)

        if (usuario == null) throw NotFoundException("Id de usuario: ${query.id} no encontrado")

        return usuario.toPrimitives()
    }
} //comentario de alejooo