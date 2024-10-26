package utn.methodology.infrastructure.persistence.repositories


import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import utn.methodology.domain.contracts.repositoriousuario
import utn.methodology.domain.entities.Usuario

class RepositorioUsuario (private val database: MongoDatabase) : repositoriousuario{

    private val collection: MongoCollection<Any> = database.getCollection("users") as MongoCollection<Any>;


   override fun guardarOActualizar(usuario: Usuario) {
        val opcion = UpdateOptions().upsert(true)
        val filtrar = Document("uuid", usuario.getId())
        val actualizar = Document("\$set", usuario.toPrimitives())

        collection.updateOne(filtrar, actualizar, opcion) 
    }

    override fun delete (usuario:Usuario) {
        val filtrar = Document("uuid", usuario.getId());
        collection.deleteOne(filtrar)
    }

    override fun RecuperarPorId (uuid: String): Usuario? {
        var usuario: Usuario? = null
        try {
            val filtrar = Document("uuid", uuid)


            val primitives = collection.find(filtrar).firstOrNull();


            usuario = Usuario.fromPrimitives(primitives as Map<String, Any>)
        } catch (e: Exception){ println ("no se pudo encontrar el usuario con ese id: ${e.message} ")}
        return usuario
    }

    fun recuperarPorUserName (userName: String): Usuario? {
        var usuario: Usuario? = null
        try {
            val filtrar = Document("userName", userName)


            val primitives = collection.find(filtrar).firstOrNull();

            println("primitive usuario $primitives")


            usuario = Usuario.fromPrimitives(primitives as Map<String, Any>)
        } catch (e: Exception){ println ("no se pudo encontrar el usuario con ese id: ${e.message} ")}
        return usuario
    }

    fun recuperarTodos(): List<Usuario> {
        println("andes d collection")
        val primitives = collection.find().map { it as Document }.toList()
        println("usuarios primitives $primitives")

        // Cambiar 'primitives' por 'it' en la conversión
        return primitives.map { Usuario.fromPrimitives(it as Map<String, Any>) }
    }

    fun existenciaporUsername(UserName: String): Boolean {
        val filtrar = Document("UserName", UserName)
        return collection.find(filtrar).firstOrNull() != null
    }

    fun existsByUuid(id: String): Boolean {
        val filtrar = Document("uuid", id)
        return collection.find(filtrar).firstOrNull() != null
    }

   override fun existenciaporEmail(email: String): Boolean {
        val filtrar = Document("email", email)
        return collection.find(filtrar).firstOrNull() != null
    }

    fun getFollowing(userId: String): List<String> {
        val usuario = RecuperarPorId(userId) ?: return emptyList()
        return usuario.seguidos
    }       // codigo Abi: metodo getFollowing para FollowRouter. (hice "seguidos" publico)

    fun update(usuario: Usuario) {
        val opcion = UpdateOptions().upsert(false)
        val filtrar = Document("uuid", usuario.getId())
        val actualizar = Document("\$set", usuario.toPrimitives())

        collection.updateOne(filtrar, actualizar, opcion)
    }
}
