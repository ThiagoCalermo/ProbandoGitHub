package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import java.util.UUID

class CreatePostHandler(private val postRepository: PostRepository) {

    fun handle(command: CreatePostCommand): Post {
        // Validación de longitud del mensaje
        if (command.message.length > 280) {
            throw IllegalArgumentException("El mensaje no puede exceder los 280 caracteres")
        }

        // Creación del post
        val post = Post(userId = command.userId, message = command.message)
        return postRepository.save(post)
    }
}