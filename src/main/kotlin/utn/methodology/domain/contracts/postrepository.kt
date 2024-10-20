package utn.methodology.domain.contracts
import  utn.methodology.domain.entities.Post
interface postrepository {
    fun guardaroActualizar(post: Post)
    fun findByOwnerId(userId: String): List <Post>
    fun findPostsByFollowing(followingUserIds: List<String>): List<Post>
}