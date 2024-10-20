package utn.methodology.Shared.Mother
import utn.methodology.domain.entities.Post
import io.github.serpro69.kfaker.Faker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
class PostMother {
    companion object {
        val faker = Faker()

        fun random(userId: String): Post {
            return Post.fromPrimitives(
                mapOf(
                    "id" to UUID.randomUUID().toString(),
                    "message" to faker.lorem.words(),
                    "userId" to userId,
                    "createdAt" to LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString(),
                    //"likes" to emptyList<String>()
                )
            )
        }
    }
}