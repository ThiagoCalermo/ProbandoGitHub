package utn.methodology.infrastructure.http.router

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.infrastructure.http.actions.CreateUserAction
import utn.methodology.infrastructure.persistence.connectToMongoDB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.application.queryhandlers.FindUserByIdHandler
import utn.methodology.application.queryhandlers.FindUserByUsernameHandler
import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.http.actions.FindUserByIdAction
import utn.methodology.infrastructure.http.actions.FindUserByUsernameAction
import utn.methodology.infrastructure.http.dtos.UserResponse

fun Application.userRouter() {             // NECESITAMOS UNA BASE DE DATOS MONGO
    val mongoDatabase = connectToMongoDB()      // Action y CreateUserHandler ya creados

    val userMongoUserRepository = RepositorioUsuario(mongoDatabase)

    val createUserAction = CreateUserAction(CreateUserHandler(userMongoUserRepository))

    // val updateUserAction = UpdateUserAction(UpdateUserHandler(userMongoUserRepository))
    val findUserByIdAction = FindUserByIdAction(FindUserByIdHandler(userMongoUserRepository))
    val findUserByUsernameAction = FindUserByUsernameAction(FindUserByUsernameHandler(userMongoUserRepository))


    routing {

        post("/users") {
            val body = call.receive<CreateUserCommand>()

            try {
                println("body $body")
                createUserAction.execute(body);
                call.respond(HttpStatusCode.Created, mapOf("message" to "Usuario creado con éxito"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al crear usuario")
            }
        }

        get("/users/{username}"){
            val username = call.parameters["username"]

            if (username.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "ingresar username")
                return@get
            }

            //val query = FindUserByUsernameQuery(call.parameters["userName"].toString())
            val query = FindUserByUsernameQuery(username)

            try {
                val result = findUserByUsernameAction.execute(query)
                println("salio de buscar en try $result")
                if (result.isNotEmpty()) {
                    println("envia la res")
                    val jsonResponse = Json.encodeToString(Usuario.fromPrimitives(result))
                    call.respond(HttpStatusCode.OK, jsonResponse)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (error: Exception) {
                println("da error sacando la respuesta")
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
        }

        get("/users/{id}") {
            val id = call.parameters["id"]

            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Ingresar id")
                return@get
            }

            //val query = FindUserByIdQuery(call.parameters["id"].toString())
            val query = FindUserByIdQuery(id)

            try {
                val result = findUserByIdAction.execute(query)
                if (!result.isEmpty()) {
                    val jsonResponse = Json.encodeToString(Usuario.fromPrimitives(result))
                    call.respond(HttpStatusCode.OK, jsonResponse)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (error: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
        }

        get("/users") {
            val users = userMongoUserRepository.recuperarTodos();

            val userResponse = users.map { user ->
                UserResponse(
                    uuid = user.uuid,
                    name = user.name,
                    userName = user.userName,
                    email = user.email,
                    seguidos = user.seguidos,
                    seguidores = user.seguidores
                )
            }

            call.respond(HttpStatusCode.OK, Json.encodeToString(userResponse))
        }


    }
}







