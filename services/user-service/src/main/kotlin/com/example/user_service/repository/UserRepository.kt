package com.example.user_service.repository

import com.example.user_service.model.User
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Repository
class UserRepository(
    private val dynamoDbClient: DynamoDbClient
) {

    fun save(user: User): User {
        val item = mapOf(
            "id" to AttributeValue.builder().s(user.id).build(),
            "name" to AttributeValue.builder().s(user.name).build(),
            "email" to AttributeValue.builder().s(user.email).build()
        )

        dynamoDbClient.putItem {
            it.tableName("Users")
            it.item(item)
        }

        return user
    }

    fun findById(id: String): User? {
        val response = dynamoDbClient.getItem {
            it.tableName("Users")
            it.key(mapOf("id" to AttributeValue.builder().s(id).build()))
        }

        if (!response.hasItem()) return null

        val item = response.item()
        return User(
            id = item["id"]!!.s(),
            name = item["name"]!!.s(),
            email = item["email"]!!.s()
        )
    }
}
