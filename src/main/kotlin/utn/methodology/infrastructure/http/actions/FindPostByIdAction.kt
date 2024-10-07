package utn.methodology.infrastructure.http.actions

import utn.methodology.application.queries.FindPostByIdQuery
import utn.methodology.application.queryhandlers.FindPostByIdHandler

class FindPostByIdAction(
    private val handler: FindPostByIdHandler
) {
    fun execute(query: FindPostByIdQuery): List<Map<String, String> >{
        query
            .validate()
            .let {
                return handler.handle(it)
            }
    }
}