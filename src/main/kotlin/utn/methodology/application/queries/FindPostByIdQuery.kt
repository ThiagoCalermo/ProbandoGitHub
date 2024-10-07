package utn.methodology.application.queries

class FindPostByIdQuery(
    val id: String,
    val order: String,
    val limit: Int,
    val offset: Int
) {
    fun validate(): FindPostByIdQuery {
        checkNotNull(id) { throw IllegalArgumentException("Id must be defined") }
        return this
    }
}