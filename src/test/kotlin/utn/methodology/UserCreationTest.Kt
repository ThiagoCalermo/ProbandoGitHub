package utn.methodology

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull


class UserService(private val UserRepository: UserRepository) {

    fun UserCreation(user: User): HttpResponse {
        return try {
            UserRepository.SaveorUpdate(user)
            HttpResponse(201, "Usuario creado exitosamente")
        } catch (e: Exception) {
            HttpResponse(400, "Error al crear usuario")
        }
    }
}

interface UserRepository {
    fun SaveorUpdate(user: User)
}

class UserRepositoryMock : UserRepository {

    var excepcion: Exception? = null
    var saveduser: User? = null
    override fun SaveorUpdate(user: User) {
        if (excepcion != null) {
            throw excepcion!!
        }
        saveduser = user
    }
}


data class HttpResponse(val code: Int, val message: String)


data class User(val uuid: String, val name: String, val username: String, val email: String, val password: String)


class UserCreationTest {

    private lateinit var service: UserService
    private lateinit var repositoryMock: UserRepositoryMock

    @Before
    fun setup() {
        repositoryMock = UserRepositoryMock()
        service = UserService(repositoryMock)
    }

    @Test
    fun `create_user_should_returns_201`() {

        val newUser = User("1", "Jaz", "jaz_1", "jaz@gmail.com", "12jaz")

        val answer = service.UserCreation(newUser)

        assertEquals(201, answer.code)
        assertEquals("Usuario creado exitosamente", answer.message)
        assertNotNull(repositoryMock.saveduser)
    }

    @Test
    fun `create_user_should_returns_400`() {
        val newUser = User("", "", "", "", "")
        repositoryMock.excepcion = Exception("Error al crear usuario")
        val answer = service.UserCreation(newUser)

        assertEquals(400, answer.code)
        assertEquals("Error al crear usuario", answer.message)
        assertNull(repositoryMock.saveduser)
    }
}
