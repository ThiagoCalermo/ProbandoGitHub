package utn.methodology.unit.application

import utn.methodology.application.commandhandlers.CreatePostHandler
import  utn.methodology.application.commands.CreatePostCommand
import  utn.methodology.Shared.MOCKS.MockPostRepository
import  utn.methodology.Shared.MOCKS.MockUserRepository
import  utn.methodology.Shared.Mother.PostMother
import  utn.methodology.Shared.Mother.UserMother
import  kotlin.test.BeforeTest
import  kotlin.test.Test

class CreatePostHandlerTest {
    private val mockUserRepository: MockUserRepository = MockUserRepository()
    private val mockPostRepository: MockPostRepository = MockPostRepository()

    private var sut: CreatePostHandler = CreatePostHandler( mockPostRepository, mockUserRepository)

    @BeforeTest
    fun beforeEach() {
        mockPostRepository.clean()
        mockUserRepository.clean()
    }

    @Test
    fun `should create a post and persist into database`() {
        // arrange
        val user = UserMother.random()
        val message = PostMother.faker.southPark.quotes()

        mockUserRepository.guardarOActualizar(user)

        val command = CreatePostCommand(
            message,
            user.getId()
        )

        // act
        sut.handle(command)

        // assertions
        val posts = mockPostRepository.findByOwnerId(user.getId())

        assert(posts.size == 1)
        assert(posts[0].getMessage() === message)
        assert(posts[0].GetUserId() == user.getId())
        assert(posts[0].getCreatedAt() != null)
    }

}