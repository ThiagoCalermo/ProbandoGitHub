package utn.methodology.application.queryhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

class FindPostByIdHandler(
    private val postRepository: PostRepository,
    private val repositoriousuario : RepositorioUsuario
) {
    fun handle(query: FindPostByIdQuery): List<Post> {

        if (!repositoriousuario.existsByUuid(query.id)) {
            return emptyList()
        }
        // Busca los posts del usuario con los filtros proporcionados
        val posts = postRepository.findPostsByUserWithFilters(
            query.id,      // ID del usuario para filtrar los posts
            query.order,   // Orden: ASC o DESC
            query.limit,   // Límite de posts
            query.offset   // Offset para paginación
        )

        // Si no se encontraron posts, se lanza una excepción
        if (posts.isEmpty()) {
            return emptyList()
        }

        // Retorna la lista de posts convertida en una lista de Map<String, String>
        return posts
    }
}
