package utn.methodology.application.commandhandlers

import io.ktor.server.plugins.*
import utn.methodology.application.commands.DeletePostCommand
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository

class DeletePostHandler (private val postRepository: PostRepository) {

    fun handle(command: DeletePostCommand): String {

        if(command.id.isEmpty()){
            return "enter id"
        }

        val post = postRepository.findOne(command.id.toInt())

        if (post == null) {
            throw NotFoundException("not found user with id: ${command.id}")
        }


        try {
            val result = postRepository.deletePost(command.id.toInt());
            if(result){
                return "deletion success"
            }else{
                return "eliminate post error"
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
            return "eliminate post error"
        }
    }
}