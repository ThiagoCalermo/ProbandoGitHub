package utn.methodology.infrastructure.http.actions

import utn.methodology.application.commandhandlers.CreatePostHandler
import utn.methodology.application.commands.CreatePostCommand
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import utn.methodology.domain.entities.Post
import org.bson.Document

class CreatePostAction(private val repository: PostRepository) {

    fun execute(command: CreatePostCommand) {
        validatePost(command.message)

        val post = Post(
            id = UUID.randomUUID().toString(),  // Genera un ID único
            userId = command.userId,
            message = command.message,
            createdAt = LocalDateTime.now()
        )

        repository.save(post)
    }
}