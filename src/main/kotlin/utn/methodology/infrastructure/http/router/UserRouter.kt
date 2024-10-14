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
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.application.queryhandlers.FindUserByIdHandler
import utn.methodology.application.queryhandlers.FindUserByUsernameHandler
import utn.methodology.infrastructure.http.actions.FindUserByIdAction
import utn.methodology.infrastructure.http.actions.FindUserByUsernameAction

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
        get("/users/{userName}"){
            val username = call.request.queryParameters["username"]

            if (username.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "ingresar username")
                return@get
            }

            //val query = FindUserByUsernameQuery(call.parameters["userName"].toString())
            val query = FindUserByUsernameQuery(username)

            try {
                val result = findUserByUsernameAction.execute(query)
                if (!result.isEmpty()) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (error: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
        }
        get("/users/{id}") {
            val id = call.request.queryParameters["id"]

            if (id.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Ingresar id")
                return@get
            }

            //val query = FindUserByIdQuery(call.parameters["id"].toString())
            val query = FindUserByIdQuery(id)

            try {
                val result = findUserByIdAction.execute(query)
                if (!result.isEmpty()) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (error: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
        }

//        get("/users") {
//            val users = userMongoUserRepository.findAll();
//
//            call.respond(HttpStatusCode.OK, users.map { it.toPrimitives() })
//        }


    }
}







