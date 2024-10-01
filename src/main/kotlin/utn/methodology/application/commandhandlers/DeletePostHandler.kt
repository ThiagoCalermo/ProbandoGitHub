package utn.methodology.application.commandhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.commands.DeletePostCommand
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository

class DeletePostHandler (private val postRepository: PostRepository) {

    fun handle(command: DeletePostCommand) {

        val user = PostRepository.findOne(command.id)

        if (user == null) {
            throw NotFoundException("not found user with id: ${command.id}")
        }

        PostRepository.delete(user);
    }
}