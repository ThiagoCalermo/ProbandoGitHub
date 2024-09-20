package utn.methodology.infrastructure.http.router

import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.infrastructure.http.actions.CreatePostAction
import utn.methodology.infrastructure.persistence.connectToMongoDB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.CreatePostHandler
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queryhandlers.FindUserByIdHandler
import utn.methodology.infrastructure.http.actions.FindUserByIdAction



fun Application.postRouter() {
    val mongoDatabase = connectToMongoDB() // Conexión a MongoDB
    val postRepository = PostRepository(mongoDatabase)
    val createPostAction = CreatePostAction(postRepository)

    routing {
        post("/posts") {
            try {
                val body = call.receive<CreatePostCommand>()
                createPostAction.execute(body)

                call.respond(HttpStatusCode.Created, mapOf("message" to "Post creado exitosamente"))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        delete("/posts/{id}") {
            // Obtener el postId desde los parámetros de la ruta
            val postId = call.parameters["id"]?.toIntOrNull()

            // Obtener el userId desde los parámetros de la query (esto puede variar dependiendo de tu autenticación)
            val userId = call.request.queryParameters["user_id"]?.toIntOrNull()

            // Validar que los IDs no sean nulos
            if (postId == null || userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Parámetros inválidos")
                return@delete
            }

            // Intentar eliminar el post usando el servicio
            val deleted = postService.deletePostById(postId, userId)

            if (deleted) {
                call.respond(HttpStatusCode.OK, "Post eliminado exitosamente")
            } else {
                call.respond(HttpStatusCode.NotFound, "Post no encontrado o no pertenece al usuario")
            }
        }
    }
}

