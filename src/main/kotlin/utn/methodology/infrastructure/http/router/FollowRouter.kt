package utn.methodology.infrastructure.http.router

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

fun Application.followRouting(
    database: MongoDatabase,
    postRepository: PostRepository,
    userRepository: RepositorioUsuario
) {

    routing {
        get("/posts/user/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            // Obtener los usuarios que el usuario actual sigue
            val followedUsers = userRepository.getFollowing(userId) // Assuming getFollowing exists in UsuarioRepository

            // Obtener los posts de los usuarios seguidos y ordenarlos por fecha
            val posts = postRepository.findPostsByFollowing(followedUsers)
                //.sort(descending(Post::createdAt))       VER: COMENTADO, REVISAR SI FUNCIONA O NO
                //.toList()

            call.respond(posts)
        }

    }
}