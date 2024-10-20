package utn.methodology.domain.contracts

import utn.methodology.domain.entities.Usuario

interface repositoriousuario {
    fun guardarOActualizar(usuario: Usuario)
    fun RecuperarPorId (uuid: String): Usuario?
    fun delete (usuario:Usuario)
    fun existenciaporEmail(email: String): Boolean
}