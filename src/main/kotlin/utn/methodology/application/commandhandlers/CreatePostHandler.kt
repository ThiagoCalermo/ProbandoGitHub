package utn.methodology.application.commandhandlers

//import utn.methodology.infrastructure.persistence.repositories.PostRepository
import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.domain.contracts.postrepository
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
import java.util.*

class CreatePostHandler(private val postRepository: postrepository, private val repositoriousuario : RepositorioUsuario) {

    fun handle(command: CreatePostCommand): String {

        if (!repositoriousuario.existsByUuid(command.userId)) {
            return "Error: debe existir el usuario."
        }
        // Validación de longitud del mensaje
        if (command.message.length > 280) {
            throw IllegalArgumentException("El mensaje no puede exceder los 280 caracteres")
        }

        // Creación del post
        val post = Post(id = UUID.randomUUID().toString(), command.userId, message = command.message)

        return try {
            postRepository.guardaroActualizar(post)
            return "post creado exitosamente"
        } catch (e: Exception) {
            "Error al crear el post: ${e.message}"
        }
    }
}
