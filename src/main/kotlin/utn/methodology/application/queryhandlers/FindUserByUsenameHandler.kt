package utn.methodology.application.queryhandlers

import utn.methodology.application.queries.FindUserByUsernameQuery
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindUserByIdQuery

class FindUserByUsenameHandler(
    private val usuarioRepositorio: MongoUserRepository
) {

    fun handle(query: FindUserByUsernameQuery): Map<String, String> {

        val usuario = usuarioRepositorio.findOne(query.userName)

        if (usuario == null) throw NotFoundException("Usuario de username: ${query.userName} no encontrado")

        return usuario.toPrimitives()
    }
}