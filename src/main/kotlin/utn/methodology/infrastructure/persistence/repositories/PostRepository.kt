package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document
import utn.methodology.domain.contracts.postrepository
import utn.methodology.domain.entities.Post


class PostRepository(val database: MongoDatabase) : postrepository {

    private var colección: MongoCollection<Document> = database.getCollection("posts") as MongoCollection<Document>

    override fun guardaroActualizar(post: Post) {
        try {
            val opcion = UpdateOptions().upsert(true) //inserta
            val filtrar = Document("id", post.id)     // Filtro basado en el ID del post
            val actualizar = Document("\$set", post.toPrimitives()) // Datos a actualizar

            colección.updateOne(filtrar, actualizar, opcion) // Upsert: inserta el post
        } catch (e: Exception) {
            println("Ocurrió un error al guardar o actualizar el post: ${e.message}")
        }
    }

    fun deletePost(postId: Int): Boolean {
        val filter = Document("_id", postId)
        val result = colección.deleteOne(filter)
        return result.deletedCount > 0
    }

    fun ListAll(): List<Post> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    fun findOne(id: Int): Post? {
        val filter = Document("_id", id)
        val primitives = colección.find(filter).firstOrNull()
        return primitives?.let { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    fun findAll(): List<Post> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map {
            Post.fromPrimitives(it.toMap() as Map<String, String>)
        }
    }

    override fun findPostsByFollowing(followingUserIds: List<String>): List<Post> {
        val filter = Document("userId", Document("\$in", followingUserIds))
        return colección.find(filter)
            .toList()
            .map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    override fun findByOwnerId(userId: String): List<Post> {
        val filter = Document("userId", userId);

        val primitives = colección.find(filter)
            .sort(Document("createdAt", 1)).toList()

        return primitives.map { Post.fromPrimitives(it as Map<String,String>) }
    }
    // Nueva función con filtros de usuario, orden, límite y offset
    fun findPostsByUserWithFilters(
        userId: String,
        order: String = "DESC",    // Por defecto DESC
        limit: Int? = null,        // Sin límite por defecto
        offset: Int? = null        // Sin offset por defecto
    ): List<Post> {
        val filter = Document("userId", userId)
        val sortOrder = if (order == "ASC") 1 else -1

        var query = colección.find(filter)

        if (offset != null) {
            query = query.skip(offset)
        }
        if (limit != null) {
            query = query.limit(limit)
        }

        query = query.sort(Document("createdAt", sortOrder))

        val primitives = query.toList()
        //println("tomap " + Json.encodeToString(primitives.map { Post.fromPrimitives(it as Map<String, Any>)}))
        return primitives.map {
            Post.fromPrimitives(it as Map<String, Any>)
        }
    }

    fun findPostsByFollowedUsersWithFilters(
        followedUserIds: List<String>,
        order: String = "DESC",
        limit: Int? = null,
        offset: Int? = null
    ): List<Post> {
        // Filtro que selecciona posts de los usuarios seguidos
        val filter = Document("userId", Document("\$in", followedUserIds))
        val sortOrder = if (order == "ASC") 1 else -1

        var query = colección.find(filter)

        if (offset != null) {
            query = query.skip(offset)
        }
        if (limit != null) {
            query = query.limit(limit)
        }

        query = query.sort(Document("createdAt", sortOrder))

        val primitives = query.toList()
        return primitives.map { Post.fromPrimitives(it as Map<String, Any>) }
    }
}

