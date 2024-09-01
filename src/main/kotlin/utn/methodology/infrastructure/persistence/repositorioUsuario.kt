import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import com.domain.entities.User
import org.bson.Document


class repositorioUsuario (private val database: MongoDatabase) {

     var colección: MongoCollection<Document>

    init {
        colección = database.getCollection("usuarios") as MongoCollection<Document>
    }

    fun GuardaroActualizar (Usuario: User) {
        try {
            val opcion = UpdateOptions().upsert(true)
            val filtrar = Document("id", Usuario.getId())
            val actualizar = Document("\$set", Usuario.toPrimitives())
            colección.updateOne(filtrar, actualizar, opcion)
        }catch(e: Exception) { println ("ocurrió un error: ${e.message}")}
    }


    fun RecuperarPorId (uuid: String): User? {
        var usuario: User? = null
        try {
            val filtrar = Document("uuid", uuid)
            val primitives = collección.find(filtrar).firstOrNull()
            usuario=User.fromPrimitives(primitives as Map<String, Any>)
        } catch (e: Exception){ println ("no se pudo encontrar el usuario con ese id: ${e.message} ")}
        return usuario
    }

    fun recuperarTodos(): List<User> {
        val primitives = colección.find().map { it as Document }.toList()
        return primitives.map { User.fromPrimitives(it.toMap() as Map<String, String>) }
    }

    fun existenciaporUsername(UserName: String): Boolean {
        val filtrar = Document("UserName", UserName)
        return colección.find(filtrar).firstOrNull() != null
    }

    fun existenciaporEmail(email: String): Boolean {
        val filtrar = Document("email", email)
        return colección.find(filtrar).firstOrNull() != null
    }
}
