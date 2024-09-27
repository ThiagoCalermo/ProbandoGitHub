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
        get("/posts") {
            // Extraer parámetros de búsqueda desde la query
            val userId = call.request.queryParameters["user_id"]?.toIntOrNull()
            val order = call.request.queryParameters["order"] ?: "DESC" // Valor por defecto DESC
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10 // Valor por defecto 10
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0 // Valor por defecto 0

            // Validar que el userId no sea nulo
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Falta el parámetro 'user_id'")
                return@get
            }

            try {
                // Validar parámetros adicionales
                if (limit !in 1..100) throw IllegalArgumentException("El límite debe estar entre 1 y 100")
                if (offset < 0) throw IllegalArgumentException("El offset debe ser mayor o igual a 0")
                if (order.uppercase() !in listOf("ASC", "DESC")) throw IllegalArgumentException("El orden debe ser 'ASC' o 'DESC'")

                // Obtener posts desde el servicio
                val posts = postService.getPostsForUser(userId, order.uppercase(), limit, offset)

                // Responder con los posts obtenidos
                call.respond(HttpStatusCode.OK, posts)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Ocurrió un error inesperado")
            }
        }
    }
}
