package utn.methodology.unit.application
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.Shared.Mother.UserMother
import  utn.methodology.Shared.MOCKS.MockUserRepository
import utn.methodology.Shared.Mother.UserMother.Companion.faker
import  kotlin.test.BeforeTest
import kotlin.test.Test
import  kotlin.test.assertNotNull
class CreateUserHandlerTest {

    val mockUserRepository = MockUserRepository()

    var sut: CreateUserHandler = CreateUserHandler(mockUserRepository)

    @BeforeTest
    fun beforeEach() {
        mockUserRepository.clean()
    }

    @Test
    fun `should create a user and persist into database`() {
        // arrange
        val name= faker.name.name();
        val username = UserMother.faker.southPark.characters();
        val email = UserMother.faker.internet.email();
        val password = UserMother.faker.southPark.characters();

        val command = CreateUserCommand(
            name = name,
            userName = username,
            email = email,
            password = password
        )

        // act
        sut.handle(command)

        //assertion
        val user = mockUserRepository.findByUsername(username)

        assertNotNull(user)
        assert(user.getEmail() == command.email)
        assert(user.getUsername() == command.userName)
        assert(user.getName() == command.name)
        assert (user.getPassword() == command.password)
    }
}