package utn.methodology.Shared.MOCKS
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
import utn.methodology.domain.contracts.repositoriousuario
import utn.methodology.domain.entities.Usuario


class MockUserRepository : repositoriousuario {
    var users: Array<Usuario> = emptyArray()

    override fun guardarOActualizar(usuario: Usuario) {
        users = users.filter { it.getId() != usuario.getId() }.toTypedArray()

        users = users.plus(usuario)
    }

    override fun RecuperarPorId (uuid: String): Usuario? {
        return users.find { it.getId() == uuid }
    }

    override fun delete (usuario:Usuario) {
        users = users.filter { it.getId() != usuario.getId() }.toTypedArray()
    }
    override fun existenciaporEmail(email: String): Boolean {
        return users.any { it.getEmail() == email }
    }
    fun clean() {
        users = emptyArray();
    }

    fun findByUsername(username: String): Usuario? {
       return users.find { it.getUsername() == username }
   }

}