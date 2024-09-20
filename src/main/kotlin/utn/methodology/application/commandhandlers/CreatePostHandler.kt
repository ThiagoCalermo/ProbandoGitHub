package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreatePostCommand
import utn.methodology.domain.entities.Post
import utn.methodology.infrastructure.persistence.repositories.PostRepository
import java.util.UUID



class CreatePostHandler(
    private val postRepository : PostRepository
) {
    fun handle(command: CreatePostCommand) {
        val post = Post(
            uuid = UUID.randomUUID().toString(),
            name = command.Nombre,
            userName = command.UserName,
            email = command.email,
            password = command.Password
        )
        usuarioRepositorio.GuardaroActualizar(usuario)
    }
} //comentario de alejo aguante dnd