package utn.methodology.infrastructure.persistence.repositories


import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import utn.methodology.domain.entities.Usuario

class RepositorioUsuario (private val database: MongoDatabase) {

     private var colección: MongoCollection<Document>

    init {
        colección = database.getCollection("usuarios") as MongoCollection<Document>
    }

    fun guardarOActualizar(usuario: Usuario) {
        val opcion = UpdateOptions().upsert(true)
        val filtrar = Document("uuid", usuario.getId())
        val actualizar = Document("\$set", usuario.toPrimitives())
            .append("seguidos", usuario.seguidos)       // En lugar de usar put, usamos el método append de
            .append("seguidores", usuario.seguidores)   // la clase Document, forma adecuada de agregar
        colección.updateOne(filtrar, actualizar, opcion) // campos en MongoDB con Kotlin.
    }

    fun RecuperarPorId (uuid: String): Usuario? {
        var usuario: Usuario? = null
        try {
            val filtrar = Document("uuid", uuid)


            val primitives = colección.find(filtrar).firstOrNull();


            usuario = Usuario.fromPrimitives(primitives as Map<String, String>)
        } catch (e: Exception){ println ("no se pudo encontrar el usuario con ese id: ${e.message} ")}
        return usuario
    }

    fun recuperarTodos(): List<Usuario> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map { Usuario.fromPrimitives(primitives as Map<String, String>) }
    }

    fun existenciaporUsername(UserName: String): Boolean {
        val filtrar = Document("UserName", UserName)
        return colección.find(filtrar).firstOrNull() != null
    }

    fun existenciaporEmail(email: String): Boolean {
        val filtrar = Document("email", email)
        return colección.find(filtrar).firstOrNull() != null
    }

    fun getFollowing(userId: String): List<String> {
        val usuario = RecuperarPorId(userId) ?: return emptyList()
        return usuario.seguidos
    }       // codigo Abi: metodo getFollowing para FollowRouter. (hice "seguidos" publico)

}
