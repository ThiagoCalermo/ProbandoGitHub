package utn.methodology.application.queryhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

class FindPostsByFollowedUsersHandler(
    private val postRepository: PostRepository,
    private val repositorioUsuario: RepositorioUsuario
) {
    fun handle(query: FindPostByIdQuery): List<Post> {
        // Verificar si el usuario existe
        val usuario = repositorioUsuario.RecuperarPorId(query.id)
            ?: throw NotFoundException("Usuario no encontrado con ID: ${query.id}")

        // Obtener la lista de IDs de los usuarios seguidos
        val followedUserIds = usuario.seguidos

        if (followedUserIds.isEmpty()) {
            return emptyList()
        }

        // Obtener los posts de los usuarios seguidos con los filtros proporcionados
        val posts = postRepository.findPostsByFollowedUsersWithFilters(
            followedUserIds,
            query.order,
            query.limit,
            query.offset
        )

        if (posts.isEmpty()) {
            throw NotFoundException("No se encontraron posts de los usuarios seguidos por el usuario con ID: ${query.id}")
        }

        return posts
    }
}