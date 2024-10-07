package utn.methodology.domain.entities


class Usuario(
    private val uuid: String,
    private var name: String,
    private var userName: String,
    private var email: String,
    private var password: String,
    var seguidos: List<String> = emptyList(),
    internal var seguidores: List<String> = emptyList()   // private -> internal (no private)
){
    companion object {
        fun fromPrimitives(primitives: Map<String, String>): Usuario{
            val seguidosList = primitives["seguidos"]?.let { it as List<String> } ?: emptyList()
            val seguidoresList = primitives["seguidores"]?.let { it as List<String> } ?: emptyList()
            val usuario = Usuario(
                primitives["uuid"] as String,
                primitives["name"] as String,
                primitives["userName"] as String,
                primitives["email"] as String,
                primitives["password"] as String,
                seguidosList,
                seguidoresList


            )
            return usuario
        }
    }
    fun getId(): String {
        return this.uuid
    }


    fun agregarSeguido(usuarioASeguirId: String) {
        seguidos += usuarioASeguirId
    }


    fun quitarSeguido(usuarioASeguirId: String) {
        seguidos = seguidos.filterNot { it == usuarioASeguirId }
    }


    /*fun getSeguidos(): List<String> {
        return seguidos
    }


    fun getSeguidores(): List<String> {
        return seguidores
    }*/


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
