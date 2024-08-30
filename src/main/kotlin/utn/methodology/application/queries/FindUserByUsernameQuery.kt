package utn.methodology.application.queries


data class FindUserByUsernameQuery(
    val userName: String
) {

    fun validate(): FindUserByUsernameQuery {
        checkNotNull(userName) {throw IllegalArgumentException("Id must be defined")}
        return this
    }
}