package utn.methodology.domain.entities

class Usuario(
    private val Id: Int,
    private var Name: String,
    private var Email: String,
    private var Password: String
){
    fun getId(): Int { return Id }
    fun update(name: String, email: String, password: String) {
        Name = name
        Email = email
        Password = password
    }
    fun checkName(Name: String) {

    }
    fun checkPassword(Pass: String){

    }
    fun checkEmail(Email: String){

    }
}