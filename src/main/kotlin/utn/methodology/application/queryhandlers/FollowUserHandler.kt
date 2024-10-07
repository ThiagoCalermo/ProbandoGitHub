package utn.methodology.application.queryhandlers


import utn.methodology.domain.entities.Usuario
import utn.methodology.infrastructure.http.actions.FollowUserAction
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario


class FollowUserHandler(private val userRepository: RepositorioUsuario) {
    fun handle(action: FollowUserAction) {
        val currentUser = userRepository.RecuperarPorId(action.userId)
        val userToFollow = userRepository.RecuperarPorId(action.userToFollowId)


        // Validate input (e.g., check if users exist, prevent self-following)
        if (currentUser == null || userToFollow == null) {
            // Handle error: user not found
            return
        }


        if (currentUser.getId() == userToFollow.getId()) {
            // Handle error: self-following not allowed
            return
        }


        // Check if user is already following
        if (currentUser.seguidos.contains(userToFollow.getId())) {
            // Unfollow the user
            unfollowUser(currentUser, userToFollow)
        } else {
            // Follow the user
            followUser(currentUser, userToFollow)
        }
    }
    // Uso:
    //val followAction = FollowUserAction("user123", "user456")
    // val followUserHandler = FollowUserHandler(userRepository)
    // followUserHandler.handle(followAction)
   
    private fun followUser(currentUser: Usuario, userToFollow: Usuario) {
        currentUser.agregarSeguido(userToFollow.getId())
        userToFollow.agregarSeguido(currentUser.getId())
        userRepository.guardarOActualizar(currentUser)
        userRepository.guardarOActualizar(userToFollow)


        // Send notifications, update user feeds, etc. (optional)
    }


    private fun unfollowUser(currentUser: Usuario, userToFollow: Usuario) {
        currentUser.quitarSeguido(userToFollow.getId())
        userToFollow.quitarSeguido(currentUser.getId())
        userRepository.guardarOActualizar(currentUser)
        userRepository.guardarOActualizar(userToFollow)
    }


}
