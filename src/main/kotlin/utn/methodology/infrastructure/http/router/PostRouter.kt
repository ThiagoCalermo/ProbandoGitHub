package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utn.methodology.application.commandhandlers.CreatePostHandler
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.application.commands.DeletePostCommand
import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.application.queryhandlers.FindPostByIdHandler
import utn.methodology.application.queryhandlers.FindPostsByFollowedUsersHandler
import utn.methodology.infrastructure.http.actions.CreatePostAction
import utn.methodology.infrastructure.http.actions.DeletePostAction
import utn.methodology.infrastructure.http.actions.FindPostByIdAction
import utn.methodology.infrastructure.http.actions.FindPostsByFollowedUsersAction
import utn.methodology.infrastructure.persistence.connectToMongoDB
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario


private fun Any.execute(query: DeletePostCommand) {

}

fun Application.postRouter() {             // NECESITAMOS UNA BASE DE DATOS MONGO
    val mongoDatabase = connectToMongoDB()      // Action y CreateUserHandler ya creados

    val mongoPostRepository = PostRepository(mongoDatabase)
    val userMongoRepository = RepositorioUsuario(mongoDatabase)

    val createPostAction = CreatePostAction(CreatePostHandler(mongoPostRepository,userMongoRepository))

    val findPostsByFollowedUsersAction = FindPostsByFollowedUsersAction(FindPostsByFollowedUsersHandler(mongoPostRepository, userMongoRepository))

    // val updateUserAction = UpdateUserAction(UpdateUserHandler(userMongoUserRepository))
    val findPostByIdAction = FindPostByIdAction(FindPostByIdHandler(mongoPostRepository, userMongoRepository))


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

        get("/posts") {
            // Extraer parámetros de búsqueda desde la query
            val userId = call.request.queryParameters["user_id"]
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
                if (limit !in 1..280) throw IllegalArgumentException("El límite debe estar entre 1 y 280")
                if (offset < 0) throw IllegalArgumentException("El offset debe ser mayor o igual a 0")
                if (order.uppercase() !in listOf("ASC", "DESC")) throw IllegalArgumentException("El orden debe ser 'ASC' o 'DESC'")

                // Obtener posts desde el servicio
                val posts = findPostByIdAction.execute(FindPostByIdQuery(userId.toString(), order.uppercase(), limit, offset))

                println("sale de obtener los posts")
                println("los posts son $posts")
                println("esta vacio " + posts.isEmpty())
                // Responder con los posts obtenidos
                call.respond(HttpStatusCode.OK, Json.encodeToString(posts))
            } catch (e: IllegalArgumentException) {
                println("error de obtener los posts")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                println("exeption  de obtener los posts")
                call.respond(HttpStatusCode.InternalServerError, "Ocurrió un error inesperado")
            }
        }

        get("/posts/user/{userId}") {
            // Extraer el parámetro de ruta `userId`
            val userId = call.parameters["userId"]
            val order = call.request.queryParameters["order"] ?: "DESC" // Valor por defecto DESC
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10 // Valor por defecto 10
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0 // Valor por defecto 0

            // Validar que el userId no sea nulo
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Falta el parámetro 'userId'")
                return@get
            }

            try {
                // Validar parámetros adicionales
                if (limit !in 1..280) throw IllegalArgumentException("El límite debe estar entre 1 y 280")
                if (offset < 0) throw IllegalArgumentException("El offset debe ser mayor o igual a 0")
                if (order.uppercase() !in listOf("ASC", "DESC")) throw IllegalArgumentException("El orden debe ser 'ASC' o 'DESC'")

                // Obtener los posts de las personas seguidas por el usuario
                val posts = findPostsByFollowedUsersAction.execute(
                    FindPostByIdQuery(userId.toString(), order.uppercase(), limit, offset)
                )

                println("sale de obtener los posts de seguidos")
                println("los posts son $posts")
                println("esta vacio " + posts.isEmpty())

                // Responder con los posts obtenidos
                call.respond(HttpStatusCode.OK, Json.encodeToString(posts))
            } catch (e: IllegalArgumentException) {
                println("error de obtener los posts de seguidos")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                println("excepción al obtener los posts de seguidos")
                call.respond(HttpStatusCode.InternalServerError, "Ocurrió un error inesperado")
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

            val query = DeletePostCommand(call.parameters["id"].toString())

            DeletePostAction.execute(query)

            call.respond(HttpStatusCode.NoContent)
        }

    }
}
