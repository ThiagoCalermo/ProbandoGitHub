package utn.methodology.infrastructure.http.router

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Sorts.descending
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.domain.entities.Post
import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.persistence.repositories.PostRepository

fun Application.followRouting() {

    routing {
        get("/posts/user/{userId}") {
            val userId = call.parameters["uuid"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)

            // Obtener una instancia de tu base de datos MongoDB        NOMBRE DE BASE DE DATOS??¿¿
            val database = MongoDatabase.createClient("mongodb://localhost:27017/grupo13metodolog-asis").getDatabase("grupo13metodolog-asis")

            // Obtener los usuarios que el usuario actual sigue
            val followedUsers = database.getCollection<Usuario>()
                .findOneById(userId)
                ?.following ?: emptyList()

            // Obtener los posts de los usuarios seguidos y ordenarlos por fecha
            val posts = database.getCollection<PostRepository>()           // POST REPOSITORY O POST?????¿¿
                .find(Post::uuid `in` followedUsers)
                .sort(descending(Post::createdAt))
                .toList()

            call.respond(posts)
        }

    }
}