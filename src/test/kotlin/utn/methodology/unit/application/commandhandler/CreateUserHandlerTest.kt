package utn.methodology.unit.application.commandhandler

import utn.methodology.Shared.MOCKS.MockUserRepository
import utn.methodology.Shared.Mother.UserMother
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.commands.CreateUserCommand
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith

class CreateUserHandlerTest {

    private val mockUserRepository = MockUserRepository()
    private lateinit var sut: CreateUserHandler

    @BeforeTest
    fun beforeEach() {
        mockUserRepository.clean()
        sut = CreateUserHandler(mockUserRepository)
    }

    @Test
    fun create_user_should_returns_201() {
        // arrange
        val name = UserMother.faker.name.name()
        val username = UserMother.faker.southPark.characters()
        val email = UserMother.faker.internet.email()
        val password = UserMother.faker.southPark.characters()

        val command = CreateUserCommand(
            name = name,
            userName = username,
            email = email,
            password = password
        )

        // act
        sut.handle(command)

        // assertion
        val user = mockUserRepository.findByUsername(username)
        assertNotNull(user)
        assert(user.email == command.email)
        assert(user.userName == command.userName)
        assert(user.name == command.name)
        assert(user.password == command.password)
    }

    @Test
    fun create_user_should_returns_400() {
        // arrange
        val commandWithoutEmail = CreateUserCommand(
            name = UserMother.faker.name.name(),
            userName = UserMother.faker.southPark.characters(),
            email = "", // Email vacío
            password = UserMother.faker.southPark.characters()
        )

        // act & assert
        val exception = assertFailsWith<IllegalArgumentException> {
            sut.handle(commandWithoutEmail)
        }

        assert(exception.message == "Email es requerido")
    }
}