package utn.methodology.application.queryhandlers


import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario


class FindUserByIdHandler(
    private val usuarioRepositorio: RepositorioUsuario
) {


    fun handle(query: FindUserByIdQuery): Map<String, Any> {


        val usuario = usuarioRepositorio.RecuperarPorId(query.id)
            ?: throw NotFoundException("Id de usuario: ${query.id} no encontrado")


        return usuario.toPrimitives()
    }
} //comentario de alejooo