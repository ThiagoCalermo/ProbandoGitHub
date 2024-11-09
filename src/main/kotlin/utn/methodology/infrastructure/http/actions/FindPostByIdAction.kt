package utn.methodology.infrastructure.http.actions

import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.application.queryhandlers.FindPostByIdHandler
import utn.methodology.domain.entities.Post

class FindPostByIdAction(
    private val handler: FindPostByIdHandler
) {
    fun execute(query: FindPostByIdQuery): List<Post>{
        query
            .validate()
            .let {
                return handler.handle(it)
            }
    }
}