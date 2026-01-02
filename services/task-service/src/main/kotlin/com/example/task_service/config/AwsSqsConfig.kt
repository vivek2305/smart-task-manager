package com.example.task_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class AwsSqsConfig {

    @Bean
    fun sqsClient(): SqsClient =
        SqsClient.builder()
            .endpointOverride(URI.create("http://localhost:4566"))
            .region(Region.US_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test")
                )
            )
            .build()
}
