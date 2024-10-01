package utn.methodology.application.commands

class DeletePostCommand(val id: String) {

    fun validate(): DeletePostCommand {
            checkNotNull(id) { throw IllegalArgumentException("Id must be defined") }
            return this
    }
}
