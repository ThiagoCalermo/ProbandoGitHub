package utn.methodology.domain.entities

import java.time.LocalDateTime

class Post(
    var id: Int? = null,    // El id se genera automáticamente al persistir
    var userId: String,
    var message: String,
    var createdAt: String = LocalDateTime.now().toString()              // VER DESCENDING
) {
    companion object {
        fun fromPrimitives(primitives: Map<String, String>): Post {
            val post = Post(
                primitives["id"] as Int?,
                primitives["userId"] as String,
                primitives["message"] as String,
                primitives["createdAt"] as String
            )
            return post
        }

    }
    fun getId(): Int? {
        return this.id
    }

    fun update(id: Int?, userId: String, message: String, createdAt: String){
        this.id = id
        this.userId = userId
        this.message = message
        this.createdAt = createdAt
    }
    fun toPrimitives(): Map<String, String> {
        return mapOf(
            "id" to this.id.toString(),
            "userId" to this.userId,
            "message" to this.message,
            "createdAt" to this.createdAt
        )
    }
}


