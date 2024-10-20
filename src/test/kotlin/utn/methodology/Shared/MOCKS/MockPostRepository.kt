package utn.methodology.Shared.MOCKS
import utn.methodology.domain.contracts.postrepository
import utn.methodology.domain.entities.Post
class MockPostRepository : postrepository {

    private var posts: Array<Post> = emptyArray()

    override fun guardaroActualizar(post: Post) {
        this.posts = this.posts.filter { it.getId() != post.getId() }.toTypedArray()
        this.posts = this.posts.plus(post)
    }

    override fun findByOwnerId(userId: String): List<Post> {
        return this.posts.filter { it.GetUserId() == userId }.toList()
    }

    override fun findPostsByFollowing(followingUserIds: List<String>): List<Post> {
        return this.posts.filter { followingUserIds.contains(it.GetUserId()) }.toList()
    }

    fun clean() {
        posts = emptyArray();
    }


}