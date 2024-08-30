package utn.methodology.infrastructure.http.actions

import utn.methodology.application.queries.FindUserByIdQuery
import utn.methodology.application.queryhandlers.FindUserByIdHandler

class FindUserByIdAction(
    private val handler: FindUserByIdHandler
) {

    fun execute(query: FindUserByIdQuery): Map<String, String> {
        query
            .validate()
            .let {
                return handler.handle(it)
            }

    }
}