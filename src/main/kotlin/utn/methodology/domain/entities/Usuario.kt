package utn.methodology.domain.entities

class Usuario(
    private val uuid: String,
    private var name: String,
    private var userName: String,
    private var email: String,
    private var password: String
){
    companion object {
        fun fromPrimitives(primitives: Map<String, String>): Usuario{
            val usuario = Usuario(
                primitives["uuid"] as String,
                primitives["name"] as String,
                primitives["userName"] as String,
                primitives["email"] as String,
                primitives["password"] as String
            )
            return usuario
        }
    }
    fun getId(): String {
        return this.uuid
    }
    fun update(name: String, userName: String, email: String, password: String){
        this.name = name
        this.userName = userName
        this.email = email
        this.password = password
    }
    fun toPrimitives(): Map<String, String> {
        return mapOf(
            "uuid" to this.uuid.toString(),
            "name" to this.name,
            "userName" to this.userName,
            "email" to this.email,
            "password" to this.password
        )
    }
}