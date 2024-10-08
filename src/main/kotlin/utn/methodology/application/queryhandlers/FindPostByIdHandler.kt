package utn.methodology.application.queryhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.infrastructure.persistence.repositories.PostRepository

class FindPostByIdHandler(
    private val postRepository: PostRepository
) {
    fun handle(query: FindPostByIdQuery): List<Map<String, String>> {
        // Busca los posts del usuario con los filtros proporcionados
        val posts = postRepository.findPostsByUserWithFilters(
            query.id,      // ID del usuario para filtrar los posts
            query.order,   // Orden: ASC o DESC
            query.limit,   // Límite de posts
            query.offset   // Offset para paginación
        )

        // Si no se encontraron posts, se lanza una excepción
        if (posts.isEmpty()) {
            throw NotFoundException("No se encontraron posts para el usuario con ID: ${query.id}")
        }

        // Retorna la lista de posts convertida en una lista de Map<String, String>
        return posts.map { it.toPrimitives() }
    }
}