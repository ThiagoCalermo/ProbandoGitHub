package utn.methodology.application.queries


data class FindUserByUsernameQuery(
    val userName: String
) {

    fun validate(): FindUserByUsernameQuery {
        checkNotNull(userName) {throw IllegalArgumentException("Debe ser definido un nombre de usuario")}
        return this
    }
}