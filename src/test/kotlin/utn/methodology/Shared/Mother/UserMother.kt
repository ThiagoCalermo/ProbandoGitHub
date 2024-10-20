package utn.methodology.Shared.Mother

import utn.methodology.domain.entities.Usuario
import io.github.serpro69.kfaker.Faker
import java.util.UUID

class UserMother {
    companion object {
        val faker = Faker()

        fun random(): Usuario {
            return Usuario.fromPrimitives(
                mapOf(
                    "uuid" to UUID.randomUUID().toString(),
                    "name" to faker.name.name(),
                    "userName" to faker.southPark.characters(),
                    "email" to faker.internet.email(),
                    "password" to faker.southPark.characters(),
                    "seguidos" to emptyList<String>().toString(),
                    "seguidores" to emptyList<String>().toString()
                )
            )
        }
    }
}