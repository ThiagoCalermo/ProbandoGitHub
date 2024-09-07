package utn.methodology.application.queryhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

class FindUserByIdHandler(
    private val usuarioRepositorio: RepositorioUsuario
) {

    fun handle(query: FindUserByUsernameQuery): Map<String, String> {

        //val usuario = repositorioUsuario.findOne(query.id)
        val usuario = usuarioRepositorio.RecuperarPorId(query.userName)

        if (usuario == null) throw NotFoundException("Id de usuario: ${query.userName} no encontrado")

        return usuario.toPrimitives()
    }
} //comentario de alejooo