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
import utn.methodology.infrastructure.persistence.repositories.repositorioUsuario
import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.infrastructure.http.actions.FindUserByIdAction
import utn.methodology.infrastructure.http.actions.FindUserByUsernameAction
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

fun Application.userRouter() {             // NECESITAMOS UNA BASE DE DATOS MONGO
    val mongoDatabase = connectToMongoDB()      // Action y CreateUserHandler ya creados

    val userMongoUserRepository = RepositorioUsuario(mongoDatabase)

    val createUserAction = CreateUserAction(CreateUserHandler(userMongoUserRepository))

    //val updateUserAction = UpdateUserAction(UpdateUserHandler(userMongoUserRepository))
    //val findUserByIdAction = FindUserByIdAction(FindUserByIdHandler(userMongoUserRepository))


    routing {

        post("/users") {
            val body = call.receive<CreateUserCommand>()
            createUserAction.execute(body);

            call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
        }
        get("/users/{userName}"){
            val query = FindUserByUsernameQuery(call.parameters["id"].toString())

            val result = FindUserByUsernameAction.execute(query)

            call.respond(HttpStatusCode.OK, result)

        }


        get("/users/{id}") {

            val query = FindUserByIdQuery(call.parameters["id"].toString())

            val result = FindUserByIdAction.execute(query)

            call.respond(HttpStatusCode.OK, result)

        }

//        get("/users") {
//            val users = userMongoUserRepository.findAll();
//
//            call.respond(HttpStatusCode.OK, users.map { it.toPrimitives() })
//        }


    }
}







