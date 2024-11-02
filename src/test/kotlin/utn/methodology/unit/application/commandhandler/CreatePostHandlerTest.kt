package utn.methodology.unit.application.commandhandler
import utn.methodology.infrastructure.persistence.repositories.RepositorioUsuario
import utn.methodology.Shared.MOCKS.MockPostRepository
import utn.methodology.Shared.MOCKS.MockUserRepository
import utn.methodology.Shared.Mother.PostMother
import utn.methodology.Shared.Mother.UserMother
import utn.methodology.application.commandhandlers.CreatePostHandler
import utn.methodology.application.commands.CreatePostCommand
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CreatePostHandlerTest {
    private val mockUserRepository: MockUserRepository = MockUserRepository()
    private val mockPostRepository: MockPostRepository = MockPostRepository()

    private lateinit var sut: CreatePostHandler

    @BeforeTest
    fun beforeEach() {
        mockPostRepository.clean()
        mockUserRepository.clean()
        sut = CreatePostHandler(mockPostRepository, mockUserRepository)
    }

    @Test
    fun create_post_should_returns_201() {
        // arrange
        val user = UserMother.random()
        val message = PostMother.faker.southPark.quotes()

        mockUserRepository.guardarOActualizar(user)

        val command = CreatePostCommand(
            message = message,
            userId = user.getId()
        )

        // act
        sut.handle(command)

        // assertions
        val posts = mockPostRepository.findByOwnerId(user.getId())

        assert(posts.size == 1)
        assert(posts[0].message == message)
        assert(posts[0].GetUserId() == user.getId())
        // assert(posts[0].getCreatedAt() != null)
    }

    @Test
    fun create_post_should_returns_400() {
        // arrange
        val user = UserMother.random()
        mockUserRepository.guardarOActualizar(user)

        // Intentar crear un post con un mensaje vacío
        val commandWithEmptyMessage = CreatePostCommand(
            message = "", // Mensaje vacío
            userId = user.getId()
        )

        // act & assert
        val exception = assertFailsWith<IllegalArgumentException> {
            sut.handle(commandWithEmptyMessage)
        }

        assert(exception.message == "Mensaje es requerido")

        // Intentar crear un post con un usuario que no existe
        val commandWithInvalidUserId = CreatePostCommand(
            message = PostMother.faker.southPark.quotes(),
            userId = "-1" // ID de usuario inválido
        )

        // act & assert
        val exceptionForInvalidUser = assertFailsWith<IllegalArgumentException> {
            sut.handle(commandWithInvalidUserId)
        }

        assert(exceptionForInvalidUser.message == "Usuario no existe")
    }
}