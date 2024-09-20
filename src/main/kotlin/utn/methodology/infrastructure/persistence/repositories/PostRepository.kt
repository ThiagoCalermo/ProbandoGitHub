package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import utn.methodology.domain.entities.Post
import org.bson.Document


class PostRepository (private val database: MongoDatabase) {

    fun save(post: Post) {
        // Aquí implementas la lógica para guardar el post en MongoDB
        val postsCollection = database.getCollection("posts")
        val postDocument = Document()
            .append("id", post.id)
            .append("userId", post.userId)
            .append("message", post.message)
            .append("createdAt", post.createdAt.toString())

        postsCollection.insertOne(postDocument)
    }


}
