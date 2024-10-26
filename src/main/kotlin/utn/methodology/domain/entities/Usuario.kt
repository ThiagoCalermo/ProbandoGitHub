package utn.methodology.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class Usuario(
    val uuid: String,
    var name: String,
    var userName: String,
    var email: String,
    var password: String,
    var seguidos: List<String> = emptyList(),
    var seguidores: List<String> = emptyList()   // private -> internal (no private)
){
    companion object {
        fun fromPrimitives(primitives: Map<String, Any>): Usuario{
            val seguidosList = primitives["seguidos"]?.let { it as List<String> } ?: emptyList()
            val seguidoresList = primitives["seguidores"]?.let { it as List<String> } ?: emptyList()
            val usuario = Usuario(
                primitives["uuid"] as String,
                primitives["name"] as String,
                primitives["userName"] as String,
                primitives["email"] as String,
                primitives["password"] as String
            )

            usuario.seguidos = seguidosList
            usuario.seguidores = seguidoresList

            return usuario
        }
    }
    // Método getter para acceder a la propiedad password
    // fun getPassword(): String {
       // return this.password
    //}
    //fun getUsername(): String {
    //    return this.userName
    //}

    fun getId(): String {
        return this.uuid
    }


    fun agregarSeguido(usuarioASeguirId: String) {
        seguidos += usuarioASeguirId
    }

    fun agregarSeguidor(seguidorId: String) {
        seguidores += seguidorId
    }

    fun quitarSeguidor(seguidorId: String) {
        seguidores = seguidores.filterNot { it == seguidorId }
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
    //fun getEmail (): String{
    //    return this.email
    //}
    //fun getPassword (): String { return this.password}
    //fun getName ():String {return  this.name}

    fun update(name: String, userName: String, email: String, password: String, seguidos: List<String>, seguidores: List<String>){
        this.name = name
        this.userName = userName
        this.email = email
        this.password = password
        this.seguidos = seguidos
        this.seguidores = seguidores
    }

    fun toPrimitives(): Map<String, Any> {
        return mapOf(
            "uuid" to this.uuid.toString(),
            "name" to this.name,
            "userName" to this.userName,
            "email" to this.email,
            "password" to this.password,
            "seguidos" to this.seguidos,
            "seguidores" to this.seguidores
        )
    }
}
