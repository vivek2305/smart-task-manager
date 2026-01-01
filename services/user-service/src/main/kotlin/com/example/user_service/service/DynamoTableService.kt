package com.example.user_service.service

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

@Service
class DynamoTableService(
    private val dynamoDbClient: DynamoDbClient
) {

    @PostConstruct
    fun createUserTableIfNotExists() {
        try {
            dynamoDbClient.describeTable {
                it.tableName("Users")
            }
        } catch (e: ResourceNotFoundException) {
            createUserTable()
        }
    }

    private fun createUserTable() {
        val request = CreateTableRequest.builder()
            .tableName("Users")
            .keySchema(
                KeySchemaElement.builder()
                    .attributeName("id")
                    .keyType(KeyType.HASH)
                    .build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder()
                    .attributeName("id")
                    .attributeType(ScalarAttributeType.S)
                    .build()
            )
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()

        dynamoDbClient.createTable(request)
    }
}

