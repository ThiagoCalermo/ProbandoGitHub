package utn.methodology.domain.entities

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class Post(
    var id: String,    // El id se genera automáticamente al persistir
    var userId: String,
    var message: String,
    var createdAt: String = LocalDateTime.now().toString()
) {
    companion object {
        fun fromPrimitives(primitives: Map<String, Any>): Post {
            val post = Post(
                primitives["id"] as String,
                primitives["userId"] as String,
                primitives["message"] as String,
                primitives["createdAt"] as String
            )
            return post
        }

    }

    //fun getId(): Int? {
    //    return this.id
    //}


    fun update(id: String, userId: String, message: String, createdAt: String){
        this.id = id
        this.userId = userId
        this.message = message
        this.createdAt = createdAt
    }
    fun toPrimitives(): Map<String, Any> {
        return mapOf(
            "id" to this.id.toString(),
            "userId" to this.userId,
            "message" to this.message,
            "createdAt" to this.createdAt
        )
    }
    fun GetUserId(): String{ return  this.userId}
    //fun getMessage(): String{return  this.message}
    //fun getCreatedAt() : String{return  this.createdAt}
}


