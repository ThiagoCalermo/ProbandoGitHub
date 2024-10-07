package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import utn.methodology.domain.entities.Post


class PostRepository (val database: MongoDatabase) {

    private var colección: MongoCollection<Document> = database.getCollection("posts") as MongoCollection<Document>

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

    fun deletePost(postId: Int): Boolean {  // ABI: FUNCION DELETE CORREGIDA?
        val filter = Document("_id", postId)  // Usa directamente el postId en lugar de Post.getId()

        val result = colección.deleteOne(filter)  // Intenta eliminar el documento con ese ID

        // Comprueba si se eliminó un documento y devuelve true si fue exitoso
        return result.deletedCount > 0
    }

    // FUNCION ANTERIOR A DELETEPOST:
    // fun delete(post: Post) {
    //        val filter = Document("_id", post.getId());
    //
    //        colección.deleteOne(filter)
    //    }

    fun ListAll(): List<Post> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    fun findOne(id: Int): Post? {
        val filter = Document("_id", id)

        val primitives = colección.find(filter).firstOrNull()

        if (primitives == null) {
            return null
        }

        return Post.fromPrimitives(primitives as Map<String, String>)
    }
    fun findAll(): List<Post> {

        val primitives = colección.find().map { it as Document }.toList();

        return primitives.map {
            Post.fromPrimitives(it.toMap() as Map<String, String>)
        }
    }


    fun findPostsByFollowing(followingUserIds: List<String>): List<Post> {
        val filter = Document("userId", Document("\$in", followingUserIds))
        return colección.find(filter)                               // funcion abi para encontrar posts
            //.sort(descending(Post::createdAt))   SORT COMENTADO: VER SI FUNCIONA O NO
            .toList()
            .map { Post.fromPrimitives(it.toMap() as Map<String, String>) }
    }


}
