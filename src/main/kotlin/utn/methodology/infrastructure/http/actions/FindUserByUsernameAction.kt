package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.infrastructure.http.actions.CreateUserAction
//import utn.methodology.infrastructure.persistence.MongoUserRepository
import utn.methodology.infrastructure.persistence.connectToMongoDB
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.queries.FindUserByUsernameQuery
import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario

class FindUserByUsernameAction {

    fun Application.userRouter() {
        val mongoDatabase = connectToMongoDB()

        val userMongoUserRepository = RepositorioUsuario(mongoDatabase)

        //val FindUserByUsernameAction = FindUserByUsernameAction(CreateUserHandler(userMongoUserRepository))

        //val findUserByUsernameAction = FindUserByUsernameAction(FindUserByIdHandler(userMongoUserRepository))


        routing {    // GET: RECUPERAR DATOS PARA SU BUSQUEDA

            get("/users/{Usuario}") {    // username o "Nombre"?
                val username = call.parameters["Usuario"]?: throw BadRequestException("Es requerido un nombre de usuario")
                val query = FindUserByUsernameQuery(username)

                val user = FindUserByUsernameAction.execute(query)

                call.respond(user?.let { HttpStatusCode.OK } ?: HttpStatusCode.NotFound, user)
            }

        }


        //get("/users") {
            //val users = userMongoUserRepository.findAll();

            //call.respond(HttpStatusCode.OK, users.map { it.toPrimitives() })
        //}
}
    }