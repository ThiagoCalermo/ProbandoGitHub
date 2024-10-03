package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.UpdateOptions
import utn.methodology.domain.entities.Post
import org.bson.Document


class PostRepository (private val database: MongoDatabase) {

    private var colección: MongoCollection<Document>

    init {
        colección = database.getCollection("posts") as MongoCollection<Document>
    }

    fun guardaroActualizar(post: Post) {
        try {
            val opcion = UpdateOptions().upsert(true) // Permite insertar o actualizar
            val filtrar = Document("id", post.id)     // Filtro basado en el ID del post
            val actualizar = Document("\$set", post.toPrimitives()) // Datos a actualizar

            colección.updateOne(filtrar, actualizar, opcion) // Upsert: inserta o actualiza el post
        } catch (e: Exception) {
            println("Ocurrió un error al guardar o actualizar el post: ${e.message}")
        }
    }
    fun deletePost(postId: Int): Boolean {

            val filter = Document("_id", Post.getId());

            colección.deleteOne(filter)

    }

    fun ListAll(): List<Post> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    fun findOne(id: Int): Post? {
        val filter = Document("_id", id);

        val primitives = colección.find(filter).firstOrNull();

        if (primitives == null) {
            return null;
        }

        return Post.fromPrimitives(primitives as Map<String, String>)
    }
    fun findAll(): List<Post> {

        val primitives = colección.find().map { it as Document }.toList();

        return primitives.map {
            Post.fromPrimitives(it.toMap() as Map<String, String>)
        };
    }

    fun delete(post: Post) {
        val filter = Document("_id", post.getId());

        colección.deleteOne(filter)
    }

    fun findPostsByFollowing(followingUserIds: List<String>): List<Post> {
        val filter = Document("userId", Document("\$in", followingUserIds))
        return colección.find(filter)                               // funcion abi para encontrar posts
            .sort(descending(Post::createdAt))
            .toList()
            .map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }


}
