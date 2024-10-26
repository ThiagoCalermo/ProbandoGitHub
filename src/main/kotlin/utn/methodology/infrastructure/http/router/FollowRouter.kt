package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.FollowUserHandler
import utn.methodology.application.commands.FollowUserCommand
import utn.methodology.application.queryhandlers.FindUserByIdHandler
import utn.methodology.infrastructure.http.actions.FindUserByIdAction
import utn.methodology.infrastructure.http.actions.FollowUserAction
import utn.methodology.infrastructure.persistence.connectToMongoDB
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

fun Application.followRouting() {
    val mongoDatabase = connectToMongoDB()      // Action y CreateUserHandler ya creados

    val userMongoUserRepository = RepositorioUsuario(mongoDatabase)

    val postRepository = PostRepository(mongoDatabase)

    val followUserAction = FollowUserAction(FollowUserHandler(userMongoUserRepository))

    // val updateUserAction = UpdateUserAction(UpdateUserHandler(userMongoUserRepository))
    val findUserByIdAction = FindUserByIdAction(FindUserByIdHandler(userMongoUserRepository))

    routing {
        get("/posts/user/{userId}") {
            val userId = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            // Obtener los usuarios que el usuario actual sigue
            val followedUsers = userMongoUserRepository.getFollowing(userId) // Assuming getFollowing exists in UsuarioRepository

            // Obtener los posts de los usuarios seguidos y ordenarlos por fecha
            val posts = postRepository.findPostsByFollowing(followedUsers)
                //.sort(descending(Post::createdAt))       VER: COMENTADO, REVISAR SI FUNCIONA O NO
                //.toList()

            call.respond(posts)
        }

        post("/users/follow") {
            val body = call.receive<FollowUserCommand>()

            try {
                println("body $body")
                followUserAction.execute(body);
                call.respond(HttpStatusCode.Created, mapOf("message" to "Usuario seguido con éxito"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al seguir usuario")
            }
        }

        post("/users/unfollow") {
            val body = call.receive<FollowUserCommand>()

            try {
                println("body $body")
                followUserAction.execute(body);
                call.respond(HttpStatusCode.Created, mapOf("message" to "Usuario dejado de seguir con éxito"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al dejar de usuario")
            }
        }
    }
}